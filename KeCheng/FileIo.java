package KeCheng;

import java.io.*;
import java.util.ArrayList;

public class FileIo {
    private static final String filePath = "data.dat";

    public static boolean saveData(ArrayList<Person> persons) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(persons);
            oos.flush();
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Person> loadData() {
        ArrayList<Person> persons = new ArrayList<Person>();
        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                persons = (ArrayList<Person>) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return persons;
    }
}
