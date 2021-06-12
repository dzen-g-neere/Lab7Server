package connection;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.*;

public class Server {
    static int BUF_SZ = 1024;
    static int port = 8647;
    Operator operator;
    ExchangeClass exchangeClass;


    public Server(Operator operator) {
        this.operator = operator;
    }

    public void process() {
        ExecutorService fixedPool = Executors.newFixedThreadPool(1);
        try {
            Selector selector = Selector.open();
            DatagramChannel channel = DatagramChannel.open();
            InetSocketAddress isa = new InetSocketAddress(port);
            channel.socket().bind(isa);
            channel.configureBlocking(false);
            SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
            clientKey.attach(new Connect());

            while (true) {
                try {
                    selector.select();
                    Iterator selectedKeys = selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        try {
                            SelectionKey key = (SelectionKey) selectedKeys.next();
                            selectedKeys.remove();

                            if (!key.isValid()) {
                                continue;
                            }

                            if (key.isReadable()) {
                                Future<Boolean> check = fixedPool.submit(() -> {
                                    try {
                                        read(key);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                });
                                while (!check.isDone()) {
                                }
                                System.out.println("done1");
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else if (key.isWritable()) {
                                long start = System.nanoTime();
                                long time = 0;

                                while (time < 10) {
                                    long finish = System.nanoTime();
                                    time = (finish - start) / 1000000000;
                                }
                                ForkJoinPool pool = new ForkJoinPool(1);
                                try {
                                    pool.submit(() -> write(key));
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                //write(key);
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        } catch (Exception eeee){

                        }
                    }
                } catch (IOException e) {
                    System.err.println("glitch, continuing... " + (e.getMessage() != null ? e.getMessage() : ""));
                }
            }
        } catch (IOException e) {
            System.err.println("network error: " + (e.getMessage() != null ? e.getMessage() : ""));
        }
        fixedPool.shutdown();
    }

    private void read(SelectionKey key) throws IOException {
        try {
            DatagramChannel channel = (DatagramChannel) key.channel();
            Connect connect = (Connect) key.attachment();
            connect.socketAddress = channel.receive(connect.request);
            ExchangeClass e = deserialize(connect.request.array());
            connect.request.rewind();
            System.out.println(e.getCommandName() + " " + e.getArgument() + "\n");
            ExecutorService pool = Executors.newCachedThreadPool();
            Future<ExchangeClass> ans = pool.submit(() -> Operator.startCommand(e));
            while (!ans.isDone()){

            }
            try {

            } catch (Exception eee){
                eee.printStackTrace();
            }
            try {
                connect.response = ByteBuffer.wrap(serialize(ans.get()));
                System.out.println(connect.response);
            } catch (Exception exception){
                exception.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Что-то пошло не так. Server  Read.");
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key) {
        DatagramChannel channel = (DatagramChannel) key.channel();
        Connect connect = (Connect) key.attachment();
        try {
            channel.send(connect.response, connect.socketAddress);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("рудфовыдао");
        }
        connect.response.rewind();
    }

    public byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public ExchangeClass deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return (ExchangeClass) o.readObject();
            }
        }
    }

    class Connect {
        ByteBuffer request;
        ByteBuffer response;
        SocketAddress socketAddress;

        public Connect() {
            request = ByteBuffer.allocate(65535);
        }
    }

}
