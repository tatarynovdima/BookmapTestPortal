import java.io.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main implements BookOrderComands{
    static final String filePathRead = "input.txt";
    static final String filePathWriter = "output.txt";

    private static Main obj = new Main('b',12,21);
    private static Main[] new_objects = new Main[200];
    private static List<Main> list;
    private String[] string;
    private Character type;
    private int size;
    private int price;

    public Main(Character type, int price, int size) {
        this.type = type;
        this.price = price;
        this.size = size;
    }
    public int getPrice() {
        return price;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getType() {
        return type;
    }


    public static String readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public static void main(String[] args) throws IOException {
        String Line;
        String text = null;
        try{
            FileWriter writer = new FileWriter(filePathWriter);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write("");
            bufferWriter.close();
        }catch (Exception e){
            System.err.println("Error if file cleaning :" + e.getMessage());
        }


        try {
            list = new LinkedList<>();
            list.sort(Comparator.comparing(Main::getPrice));
            FileReader in = new FileReader(filePathRead);
            BufferedReader reader  = new BufferedReader(in);


            text = readFile(filePathRead);

            String s = "";

            while (reader.ready()) {
                s += reader.readLine() + "\r\n";
            }

            String[] res = s.split("\r\n");

            for (int i = 0; i<res.length; i++) {
                if (res[i].charAt(0) == 'q') {
                    obj.proceedQuery(res[i]);
                } else if (res[i].charAt(0) == 'o') {
                    obj.proceedOrder(res[i]);
                } else if (res[i].charAt(0) == 'u') {
                    obj.proceedUpdate(res[i]);
                } else{
                    System.out.println("input error");
                }
            }
            in.close();
        }catch (IOException e){
            System.out.println("Error : "  + e);
        }
    }

    @Override
    public void proceedOrder(String str) {
        /* break line into words */
        string = str.split(",");
        int num = Integer.parseInt(string[2]);
        if (string[1].equals("sell")){
            obj.removeBestBid(num);
        }
        else if (string[1].equals("buy")){
            obj.removeBestAsk(num);
        }
    }

    @Override
    public void proceedUpdate(String str) {
        string = str.split(",");
        price = Integer.parseInt(string[1]);
        size = Integer.parseInt(string[2]);
        if (string[3].equals("bid")) {
            type = 'B';
            for (int i = 0; i<100; i++) {
                new_objects[i] = new Main(type, price, size);
                list.add(new_objects[i]);
                break;
            }
        } else if (string[3].equals("ask")) {
            type = 'A';
            for (int i = 0; i<100; i++) {
                new_objects[i] = new Main(type, price, size);
                list.add(new_objects[i]);
                break;
            }
        }
    }

    @Override
    public void proceedQuery(String str) throws IOException {
        string = str.split(",");

        if (string[1].equals("best_bid")) {
            obj.getBestBid();
        } else if (string[1].equals("best_ask")) {
            obj.getBestAsk();
        }
        /* for expression q,size,<price> */
        else if (string[1].equals("size")){
            int num = Integer.parseInt(string[2]);

            for (int i = 0; i < list.size(); i++) {
                if (num == list.get(i).getPrice()) {

                    try{
                        FileWriter writer = new FileWriter(filePathWriter, true);
                        BufferedWriter bufferWriter = new BufferedWriter(writer);
                        bufferWriter.write(list.get(i).getSize() + "\r\n");
                        bufferWriter.close();
                    }catch (IOException e){
                        System.out.println("Error : " + e);
                    }
                    break;
                }
            }
        }
    }
    @Override
    public void getBestBid()  throws IOException{
        for (int i = list.size()-1; i >= 0; i--) {
            if (list.get(i).getType() == 'B') {

                try{
                    FileWriter writer = new FileWriter(filePathWriter, true);
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    bufferWriter.write(list.get(i).getPrice() + "," + list.get(i).getSize() + "\r\n");
                    bufferWriter.close();
                }catch (IOException e){
                    System.out.println("Error : " + e);
                }
                break;
            }
        }
    }

    @Override
    public void removeBestBid(int num) {
        size = size-num;

        for (int i = list.size()-1; i >= 0; i--) {
            if (list.get(i).getType() == 'B') {
                list.get(i).setSize(size);
                break;
            }
        }
    }

    @Override
    public void getBestAsk() throws IOException {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 'A') {
                try{
                    FileWriter writer = new FileWriter(filePathWriter, true);
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    bufferWriter.write(list.get(i).getPrice() + "," + list.get(i).getSize() + "\r\n");
                    bufferWriter.close();
                }catch (IOException e) {
                    System.out.println("Error : " + e);
                }
                break;
            }
        }
    }

    @Override
    public void removeBestAsk(int num) {
        size = size-num;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 'A') {
                list.get(i).setSize(size);
                break;
            }
        }
    }
}