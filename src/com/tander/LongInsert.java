package com.tander;

import com.tander.xml.Entries;
import com.tander.xml.Entry;
import com.tander.xml.Parser;
import com.tander.xml.Preproccesor;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by velmOO on 12.04.2016.
 */

//Не получилось соблюдать DRY принцип - немного хардкодил (клон основоного класса)
//используем интерфейс call в тандеме с future

public class LongInsert implements Callable<String> {

    private static String dbURL;
    private static Integer N;

    public static void setDbURL(String dbURL) {
        LongInsert.dbURL = dbURL;
    }
    public static void setN(Integer n) {
        N = n;
    }


    public  LongInsert(Integer newN) {
        setN(newN);
    }

    public static void LongTask() throws SQLException, JAXBException,
            IOException, TransformerException,
            ParserConfigurationException,
            SAXException, XPathExpressionException, ExecutionException, InterruptedException  {


        setDbURL("jdbc:mysql://localhost:3306/tan");
        insertData();
        Entries entries = new Entries();
        entries.setEntires(getData());
        JAXBContext jc = JAXBContext.newInstance(Entries.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(entries, System.out);
        OutputStream os = new FileOutputStream("1.xml");
        marshaller.marshal(entries, os );
        os.close();
        Preproccesor.transform();
        int total = Parser.countField();

        System.out.println("sum of field attributes value "+total);

    }

    public static void insertData() throws SQLException, ExecutionException
    {

        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;

        CheckForPurgeTable();

        for (int i = 1; i <= N; i++) {

            try {
                connection = DriverManager.getConnection(dbURL, "ta", "654456");

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `test` (`FIELD`) VALUES (?);");
                preparedStatement.setInt(1, i);
                preparedStatement.executeUpdate();

                preparedStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }

        connection.close();
    }



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
            e.printStackTrace();
        }

        connection.close();
        return ls;
    }




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


    @Override
    public String call() throws Exception {
        LongTask();
        return "Успели меньше чем за 5 минут";
    }
}