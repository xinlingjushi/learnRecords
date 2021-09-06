package com.example.demo.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;


public class TxtToXlsx {

    public static void main(String argv[]){
        //读取的txt文件路径
        String txtFilePath = "E:\\Documents\\Desktop\\ex\\q.txt";
        //生成的excel文件路径
        String excelFilePath = "E:\\Documents\\Desktop\\ex\\数据.xlsx";
        //编码格式
        String encoding = "GBK";
        readAndWrite(txtFilePath,excelFilePath,encoding);
    }

    public static WritableCellFormat getHeaderFormat(){
        //设置字体为宋体，11号
        WritableFont headerFont = new WritableFont(WritableFont.createFont("宋体"), 11,
                WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
        WritableCellFormat headerFormat = new WritableCellFormat (headerFont);
        return headerFormat;
    }


    public static WritableCellFormat getHeaderFormatFail() throws WriteException {
        WritableFont headerFontred = new WritableFont(WritableFont.createFont("宋体"), 11,
            WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
        WritableCellFormat headerFormatred = new WritableCellFormat (headerFontred);
        headerFormatred.setBackground(Colour.RED);
        return headerFormatred;
    }


    public static void readAndWrite(String filePath,String excelFilePath,String encoding){
        try{
            File file = new File(filePath);
            File tempFile = new File(excelFilePath);
            //判断文件是否存在
            if (!file.isFile() || !file.exists()){
                System.out.println("找不到指定的文件");
            }
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            WritableWorkbook workbook = Workbook.createWorkbook(tempFile);
            int sheetint= 0;
            WritableSheet sheet = workbook.createSheet("Sheet"+sheetint, sheetint);

            WritableCellFormat wcf = getHeaderFormat();
            WritableCellFormat wcfFail = getHeaderFormatFail();

            //一些临时变量，用于写到excel中
            String lineTxt = null;
            int i = 0;
            int xz = 65536;
            while ((lineTxt = bufferedReader.readLine()) != null){
                if(i>=xz){
                    sheetint++;
                    sheet = workbook.createSheet("Sheet"+sheetint, sheetint);
                    i=0;
                }
                String[] list =  lineTxt.split("@!@");
                for (int f=0;f<list.length;f++){
                    if(f==5 && list[f].length()>1){
                        sheet.addCell(new Label(f, i, list[f], wcfFail));
                        continue;
                    }
                    sheet.addCell(new Label(f, i, list[f], wcf));
                }
                i++;
                //判断内容是否为空行，如果是，则转行
                if("\\r".equals(lineTxt)){
                    continue;
                }
            }
            //写入文件
            workbook.write();
            //关闭文件
            workbook.close();
            read.close();
        }catch (Exception e){
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

}

