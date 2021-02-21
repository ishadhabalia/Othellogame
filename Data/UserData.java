package Data;
import java.io.*;
import java.util.*;

public class UserData{
    public static int add_username(String name){

        try{
            File myObj = new File("PlayerData.csv");
            String file = "PlayerData.csv";
            FileWriter csvWriter2 = new FileWriter("PlayerData.csv", true);
            csvWriter2.append(name);
            csvWriter2.append(",");
            csvWriter2.append("0");
            csvWriter2.append(",");
            csvWriter2.append("0");
            csvWriter2.append("\n");
            csvWriter2.flush();
            csvWriter2.close();
            return 0;

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return 0;
        }
    }


    public static List<String[]> readData() {
        List<String[]> content = new ArrayList<>();
        try {
            File file = new File("PlayerData.csv");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            List<String> name = new ArrayList<>();
            List<Integer> standard = new ArrayList<>();
            List<Integer> modified = new ArrayList<>();

            //String[] tempArr;
            while ((line = br.readLine()) != null) {
                content.add(line.split(","));
                System.out.println();
            }
            for (int i = 0; i < content.size(); i++) {
                String element[] = content.get(i);
                String ele = element[0];
                int ele2 = Integer.parseInt(element[1]);
                int ele3 = Integer.parseInt(element[2]);
                name.add(ele);
                standard.add(ele2);
                modified.add(ele3);
            }
            br.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return content;
    }

    public static int check(String n) {
        List<String[]> p = new ArrayList<>();
        p = readData();
        List<String> name = new ArrayList<>();
        String ele;

        //System.out.println("data size is "+p.size());
        for (int i = 0; i < p.size(); i++) {
            String element[] = p.get(i);
            ele = element[0];

            name.add(ele);

        }
        if (name.contains(n)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static List<String[]> update(String n, int moves, int choice) {
        List<String[]> z = new ArrayList<>();
        List<String[]> z2 = new ArrayList<>();
        try {
            z = readData();
            List<String> name = new ArrayList<>();
            List<Integer> standard = new ArrayList<>();
            List<Integer> modified = new ArrayList<>();
            int index;
            for (int i = 0; i < z.size(); i++) {
                String element[] = z.get(i);
                String ele = element[0];
                int ele2 = Integer.parseInt(element[1]);
                int ele3 = Integer.parseInt(element[2]);
                name.add(ele);
                standard.add(ele2);
                modified.add(ele3);
            }
            index = name.indexOf(n);
            //standard.set(index, moves);
            if (choice == 1)
            {
                if(moves<standard.get(index)||standard.get(index)==0)
                    standard.set(index, moves);
            } else {
                if(moves<modified.get(index)||modified.get(index)==0)
                    modified.set(index, moves);
            }
            String row = "";
            for (int i = 0; i < z.size(); i++){
                row = name.get(i) + "," + String.valueOf(standard.get(i)) + "," + String.valueOf(modified.get(i));
                z2.add(row.split(","));
                //z.add(name.get(i),"0","0");
            }
            FileWriter csvWriter = new FileWriter("PlayerData.csv");
            for (int i = 0; i < z2.size(); i++) {

                csvWriter.append(name.get(i));
                csvWriter.append(",");
                csvWriter.append(String.valueOf(standard.get(i)));
                csvWriter.append(",");
                csvWriter.append(String.valueOf(modified.get(i)));
                csvWriter.append("\n");

            }
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return z;
    }

    public static void sorting(int choice) {
        List<String[]> z = new ArrayList<>();
        //List<String[]> z2 = new ArrayList<>();
        z = readData();
        List<String> name = new ArrayList<>();
        //List<Integer> standard = new ArrayList<>();
        //List<Integer> modified = new ArrayList<>();
        Vector<SortedDisplay> v = new Vector<SortedDisplay>(z.size());

        if (choice == 1) {

            for (int i = 0; i < z.size(); i++) {
                String element[] = z.get(i);
                String n = element[0];
                int e = Integer.parseInt(element[1]);
                v.add(new SortedDisplay(n, e));

                SortedDisplay temp;
                for (int j = 0; j < z.size(); j++) {
                    if (v.get(i).score <= v.get(j).score) {
                        temp = v.get(i);
                        for (int k = i; k > j; k--) {
                            v.set(k, v.get(k - 1));
                        }
                        v.set(j, temp);
                        break;
                    }
                }
            }

        } else {
            for (int i = 0; i < z.size(); i++) {
                String element[] = z.get(i);
                String n = element[0];
                int h = Integer.parseInt(element[2]);
                v.add(new SortedDisplay(n, h));

                SortedDisplay temp;
                for (int j = 0; j < z.size(); j++) {
                    if (v.get(i).score <= v.get(j).score) {
                        temp = v.get(i);
                        for (int k = i; k > j; k--) {
                            v.set(k, v.get(k - 1));
                        }
                        v.set(j, temp);
                        break;
                    }
                }
            }
        }
        System.out.format("%60s%20s\n","USERNAME", "MOVES");
        for (int i = 0; i<z.size() ; i++)
        {

            String n = v.get(i).name;
            int s = v.get(i).score;
            if(s!=0)
            {
                System.out.format("%60s%20d\n",n, s);
            }
        }
        System.out.println("\n\n");
    }
}

class SortedDisplay
{

    String name;
    int score;
    SortedDisplay(String n, int s)
    {
        name=n;
        score=s;
    }
}