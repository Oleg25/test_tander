package com.tander;


import com.tander.xml.Entries;
import com.tander.xml.Entry;
import com.tander.xml.Parser;
import com.tander.xml.Preproccesor;
import org.xml.sax.SAXException;

import java.io.*;
import java.sql.*;
import javax.xml.bind.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {


    private static String dbURL;
    private static Integer N;

    public static void setDbURL(String dbURL) {
        Main.dbURL = dbURL;
    }
    public static void setN(Integer n) {
        N = n;
    }

    public static void main (String[] args)
            throws SQLException, JAXBException,
            IOException, TransformerException,
            ParserConfigurationException,
            SAXException, XPathExpressionException, ExecutionException, InterruptedException {

        //----------- 1 ый шаг ТЗ------------
        //Инициализация через сеттеры
        setDbURL("jdbc:mysql://localhost:3306/tan");
        setN(16);

        //----------- 2 ый шаг ТЗ------------
        //Вставка данных в таблицу
        insertData();

        //------------ 3 ый шаг ТЗ------------
        //формирование xml
        Entries entries = new Entries();
        entries.setEntires(getData());

        JAXBContext jc = JAXBContext.newInstance(Entries.class);

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(entries, System.out);

        OutputStream os = new FileOutputStream("1.xml");
        marshaller.marshal(entries, os );
        os.close();

        //-------------4 ый шаг ТЗ------------
        //xslt трансформация
        Preproccesor.transform();

        //------------5 ый шаг ТЗ-------------
        //парсинг преобразованого xml
        int total = Parser.countField();
        System.out.println("sum of field attributes value "+total);


    }




    /**
     * Insert 1..N values in TEST.FIELD
     * @throws SQLException
     */
    public static void insertData() throws SQLException, ExecutionException,
            InterruptedException {

        //----------Соблюдаем доп. условие ТЗ-------------//

        //Дополнительные условия ТЗ при значении N > 10000000
        //Проверяем выполнение задачи не более 5 минут (300 сек)
        //используем чудо пакет java.util.concurrent
        if (N >100000){
            //передаем отдельному потоку на выполнение N итераций
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(new LongInsert(N));

            try {

                System.out.println(future.get(300, TimeUnit.SECONDS));

            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Задача выполняется более 5 минут и будет прервана");
                System.exit(0);
            }

            executor.shutdownNow();
            System.exit(0);

        }


        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;

        //Очищаем таблицу перед вставкой
        CheckForPurgeTable();

        for (int i = 1; i <= N; i++) {

            try {
                connection = DriverManager.getConnection(dbURL, "ta", "654456");

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `test` (`FIELD`) VALUES (?);");
                preparedStatement.setInt(1, i);
                preparedStatement.executeUpdate();

                preparedStatement.close();


            } catch (SQLException e) {
                //System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                return;
            }


        }

        connection.close();
    }


    /**
     *
     * @return List<Entry>
     * @throws SQLException
     */
    public static List<Entry> getData() throws SQLException {

        setDbURL("jdbc:mysql://localhost:3306/tan");

        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;

        List<Entry> ls = new ArrayList<Entry>();


            try {
                connection = DriverManager.getConnection(dbURL, "ta", "654456");

                st = connection.createStatement();
                rs = st.executeQuery("SELECT FIELD FROM test");

                while (rs.next()) {
                    Entry entry = new Entry();
                    entry.setField(rs.getInt("FIELD"));
                    ls.add(entry);
                }


            } catch (SQLException e) {
                //System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
            }

        connection.close();
        return ls;
    }




    /**
     * Purge TEST before insert data
     * @throws SQLException
     */
   public static void CheckForPurgeTable() throws SQLException {

       setDbURL("jdbc:mysql://localhost:3306/tan");

       Connection connection = null;
       Statement st = null;
       ResultSet rs = null;
       Integer rowcount =0;

             connection = DriverManager.getConnection(dbURL, "ta", "654456");

                 st = connection.createStatement();
                 rs = st.executeQuery("SELECT COUNT(field) FROM test");

                while (rs.next()) {
                    rowcount = rs.getInt(1);
                }


               if (rowcount >1){
                   st = connection.createStatement();
                   st.executeUpdate("DELETE from TEST");

               }

           st.close();
           rs.close();
           connection.close();

   }


}




