
import java.io.*;
import java.util.*;

public class main {
        private static final String exitMessage = " Press Enter to terminate the program.\n";

    public static void main(String[] args) throws Exception {

        String TypeSort = "-a";
        String Type = "";
        String outputFileName;
        String text;
        String[] temp =  new String[args.length]  ;
//        System.out.println("Enter sort options: -sort type(-a/-d) -data type(-i/-s)"
//                + " output file name(out.txt) input file names(in1.txt)");
//        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
//        text = read.readLine();
//        temp = text.split(" ");

       for (int j = 0; j < args.length; j++) {
            temp[j]= args[j];
        }
        if (temp.length < 3) {
            throw new Exception("Required parameters are missing:" + " data type, output file name, or input file name.");
        }
         //read.close();
        int i = 0;
        //Проверяем введенный тип сортировки
        if (temp[i].equals("-d")) {
            TypeSort = "-d";
            i++;
        } else if (temp[i].equals("-a")) {
            TypeSort = "-a";
            i++;
        } else if (temp[i].equals("-i") || temp[i].equals("-s")) {
            System.out.println("You didn't specify a sort type. The items will be sorted in ascending order.");
            TypeSort = "-a";
        }else   throw new Exception("You entered the wrong sort type, please try again.");
        //Проверяем введеный тип данных
        if (temp[i].equals("-i") || temp[i].equals("-s")) {
            if (temp[i].equals("-i")) Type = "-i";
            else Type = "-s";
            i++;
        } else throw new Exception("You entered the wrong data type, please try again.");
        //Выводимый файл
        outputFileName = temp[i];
        i++;

        String data[]; //массив строк
        int intArr[]; //массив целых чисел
        List<String> rawData = new ArrayList<>();

        for (int j = i; j < temp.length; j++) {
            ///System.out.println(new File("resources/"+temp[j]).getAbsolutePath());
            try (BufferedReader br =
                   //    new BufferedReader(new FileReader(new File("java/resources/"+temp[j]).getAbsolutePath()))) { //если используем буфер
                         new BufferedReader(new FileReader(new File("resources/"+temp[j]).getAbsolutePath()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.indexOf(' ') != -1)
                        throw new Exception("You have a space in the line, remove it and try again");
                    rawData.add(line);
                }
            } catch (FileNotFoundException e) {
                System.out.println("The input file with the given name was not found." + exitMessage);
                exitByEnterPressed();
            } catch (IOException e) {
                System.out.println("Error reading input data." + exitMessage);
                exitByEnterPressed();
            }
        }

        data = new String[rawData.size()];
        rawData.toArray(data);
        Type = checkArrayType(data, Type);
        try (BufferedWriter writer =
                  //   new BufferedWriter(new FileWriter(new File("java/resources/"+outputFileName)))) { если используем буфер
                     new BufferedWriter(new FileWriter(new File("resources/"+outputFileName)))) {
            if (Type.equals("-i")) {
                intArr = Arrays.stream(data).mapToInt(value -> Integer.parseInt(value)).toArray();
                intArr = Arrays.stream(data).mapToInt(value -> Integer.parseInt(value)).toArray();
                Comparator<Integer> com = (a, b) -> a.compareTo(b);
                mergeStartInt(intArr, TypeSort);
                for (int k = 0; k < intArr.length; k++)
                    writer.write(intArr[k] + "\n");
            } else {
                Comparator<String> comp = (a, b) -> {
                    if (a.length() != b.length()) {
                        return a.length() - b.length();
                    }
                    return a.compareTo(b);
                };//сортировка по алфавиту и длинне
                mergeStart(data, comp, TypeSort);

                for (int k = 0; k < data.length; k++)
                    writer.write(data[k] + "\n");
            }
        } catch (IOException e) {
                System.out.println("Error writing output.\n" + exitMessage);
        }

    }

    public static void exitByEnterPressed() {
        try {
            System.in.read();
            System.exit(1);
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    // Проверка соответствия выбранного типа данных типу данных в файле
    public static String checkArrayType(String[] arr, String expectedType) {
        if (expectedType.equals("-i")) {
            try {
                int intArr[] = Arrays.stream(arr).mapToInt(value -> Integer.parseInt(value)).toArray();
            } catch (NumberFormatException e) {
                System.out.println("You are trying to sort an array containing strings like an array of integers. "
                                   + "The sorted data type will be changed.");
                return "-s";
            }
        } else if (expectedType.equals("-s")) {
            try {
                int intArr[] = Arrays.stream(arr).mapToInt(value -> Integer.parseInt(value)).toArray();
                System.out.println("You are trying to sort an array of integers as an array of strings."
                                    + "The type of data being sorted will be changed.\n");
                return "-i";
            } catch (NumberFormatException e) {
                return expectedType;
            }
        } else {
            System.out.println("You specified an invalid input data type. "
                    + "The data will be sorted by default as an array of strings.\n");
            return "-s";
        }
        return expectedType;
    }

    public static void mergeStartInt(int[] array, String TypeSort) {
        int[] support = Arrays.copyOf(array, array.length);
        int startIndex = 0;
        int stopIndex = support.length - 1;
        mergeSort(array, support, startIndex, stopIndex, TypeSort);
    }
    public static void mergeSort(int[] array, int[] support, int startIndex, int endIndex, String TypeSort) {
        if (startIndex >= endIndex) { // условия окончания нашей рекурсии
            return;
        }
        int h = startIndex + (endIndex - startIndex) / 2;
        mergeSort(array, support, startIndex, h, TypeSort); //сортировка левой подпоследовательность
        mergeSort(array, support, h + 1, endIndex, TypeSort); // сортировка правой подпоследовательноси
        merge(array, support, startIndex, h, h + 1, endIndex, TypeSort); // слияние подпоследовательностей
    }
    public static void merge(int[] array, int[] supportArray, int ls, int le, int rs, int re, String TypeSort) {
        for (int i = ls; i <= re; i++) {
            supportArray[i] = array[i];
        }
        int l = ls;
        int r = rs;
        for (int i = ls; i <= re; i++) {
            if (l > le) {
                array[i] = supportArray[r];
                r += 1;
            } else if (r > re) {
                array[i] = supportArray[l];
                l += 1;
            } else if (TypeSort.equals("-a") && supportArray[l] < supportArray[r]) {//сортируем по возрастанию
                array[i] = supportArray[l];
                l += 1;
            } else if (TypeSort.equals("-d") && supportArray[l] > supportArray[r]) { // сортировка по убыванию
                array[i] = supportArray[l];
                l += 1;
            } else {
                array[i] = supportArray[r];
                r += 1;
            }
        }
    }
    //Сортировка строк
    public static <T> void mergeStart(T[] array, Comparator<T> comp, String TypeSort) {
        T[] support = Arrays.copyOf(array, array.length);
        int startIndex = 0;
        int endIndex = support.length - 1;
        mergeSort(array, support, comp, startIndex, endIndex, TypeSort);
    }
    public static <T> void mergeSort(T[] array, T[] support, Comparator<T> comp, int startIndex, int endIndex, String TypeSort) {
        if (startIndex >= endIndex) {
            return;
        }
        int h = startIndex + (endIndex - startIndex) / 2;
        mergeSort(array, support, comp, startIndex, h, TypeSort);//сортировка левой подпоследовательность
        mergeSort(array, support, comp, h + 1, endIndex, TypeSort);// сортировка правой подпоследовательноси
        merge(array, support, comp, startIndex, h, h + 1, endIndex, TypeSort);// слияние подпоследовательностей
    }
    public static <T> void merge(T[] array, T[] support, Comparator<T> comp, int ls, int le, int rs, int re, String TypeSort) {
        for (int i = ls; i <= re; i++) {
            support[i] = array[i];
        }
        int l = ls;
        int r = rs;
        for (int i = ls; i <= re; i++) {
            if (l > le) {
                array[i] = support[r];
                r += 1;
            } else if (r > re) {
                array[i] = support[l];
                l += 1;
            } else if (TypeSort.equals("-a") && comp.compare(support[l], support[r]) < 0) {
                array[i] = support[l];
                l += 1;
            } else if (TypeSort.equals("-d") && comp.compare(support[l], support[r]) > 0) {
                array[i] = support[l];
                l += 1;
            } else {
                array[i] = support[r];
                r += 1;
            }
        }
    }
}
