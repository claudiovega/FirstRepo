package cl.propiedades;

import org.springframework.util.NumberUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestPags {

    public static void main(String[] args){


        LocalDate fechaTope = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = "01/01/2020";


        //convert String to LocalDate
        fechaTope = LocalDate.parse(date, formatter);
        System.out.println(fechaTope);
//        String number = "400006686.05";
//        //1.650,00
//        Double result = null;
//        String str = "1,11,111.23d";
//        try {
//            double l = DecimalFormat.getNumberInstance().parse("400006686.05").doubleValue();
//            DecimalFormat.getNumberInstance().parse("400006686,05").doubleValue();
//            DecimalFormat.getNumberInstance().parse("1.650,00").doubleValue();
//            DecimalFormat.getNumberInstance().parse("1,11,111.23").doubleValue();
//            System.out.println(l); //111111.23
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result = Double.parseDouble(number.trim());
//       System.out.println(Double.parseDouble(""));
//        System.out.println(Double.parseDouble(""));
//        System.out.println(Double.parseDouble(""));
//        NumberUtils.parseNumber("1.650,00", Double.class);
//        NumberUtils.parseNumber("400006686.05", Double.class);
////        if (number != null && !number.trim().isEmpty()){
////            try{
////                if (!number.toLowerCase().trim().equals("null")) {
////                    if (number.contains(".") && !number.contains(",")){
////                        number = number.replace(".",",");
////                    }else if (number.contains(".") && number.contains(",")){
////                        number = number.replace(".","");
////                        number = number.replace(",",".");
////                    }
////
////                    result = Double.parseDouble(number.trim());
////                }
////            }catch(Exception e){
////                throw e;
////            }
////        }
//


    }
}
