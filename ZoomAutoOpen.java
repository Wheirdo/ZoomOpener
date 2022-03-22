import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.Random;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
public class ZoomAutoOpen{
    public static void main() {
        List <String []> data = new ArrayList<String []>();
        String filename = "Fall2020.csv"; 

        Schedule monday = new Schedule("M",readFile(filename,"M"));
        Schedule tuesday = new Schedule("Tu",readFile(filename,"Tu"));
        Schedule wednsday = new Schedule("W",readFile(filename,"W"));
        Schedule thursday = new Schedule("Th",readFile(filename,"Th"));
        Schedule friday = new Schedule("F",readFile(filename,"F"));
        Schedule [] full_schedule = {monday,tuesday,wednsday,thursday,friday};

        DateTime dateTime = new DateTime();

        int delay = 0;
        List <Integer> delays = new ArrayList<Integer>();

        int hour = 0;
        int minute = 0;
        String weekday = "";

        int s_hour = 0;
        int s_minute = 0;
        String s_weekday = "";
        int time_till = 0;       

        
        while (true) {

            delays.clear();
            dateTime.updateTime();
            dateTime.printDateTime();
            for (int x = 0; x < full_schedule.length; x++) {

                weekday = dateTime.getWeekday();
                s_weekday = full_schedule[x].getWeekday();

                if (weekday.equals(s_weekday)) {
                    for (int y = 0; y < full_schedule[x].getHours().size(); y++) {//hours classes that are today
                        time_till = 0;
                        hour = dateTime.getHour();
                        s_hour = full_schedule[x].getHours().get(y);    
                        System.out.println(s_hour);
                        minute = dateTime.getMinute();
                        s_minute = full_schedule[x].getClasses().get(y).getMinute();

                        time_till = timeTill(hour,minute,s_hour,s_minute);
                        if (time_till < 0) {
                            time_till = 90;
                        }

                        System.out.println("There is "+time_till+" minutes untill "+full_schedule[x].getClasses().get(y).getName());

                        if (time_till <= 5) {
                            System.out.println("Opening "+full_schedule[x].getClasses().get(y).getName());

                            try {
                                openChrome();
                                type(full_schedule[x].getClasses().get(y).getLink());
                                type("/ENTR/");
                                delay(2000);
                                type(full_schedule[x].getClasses().get(y).getPassword());
                                type("/ENTR/");
                            }
                            catch (Exception e) {
                                //
                            }

                            break;
                            //open class
                        }
                        else if (time_till <= 10) {
                            delays.add(1);
                        }
                        else {
                            delays.add(time_till - 10);
                        }
                    }                   
                }
            }

            delay = 90;
            for (int i = 0; i < delays.size(); i++) {
                if (delay > delays.get(i)) {
                    delay = delays.get(i);
                }
            }

            System.out.println("Delay: "+delay);
            delayMin(delay);      
        }        
    }

    //in minutes
    public static int timeTill(int start_hour, int start_minute, int end_hour, int end_minute) {
        return ((end_hour - start_hour)*60) + (end_minute - start_minute);
    }

    public static void printFullSchedule(Schedule [] arr) {
        System.out.println("---Full Schedule---");
        for (int i = 0; i < arr.length; i++) {
            arr[i].info();
        }
    }

    //Name,Nickname,Day,StartTime,Link,Password 
    public static List<Class> readFile(String filename, String weekday) {
        List <Class> data = new ArrayList<Class>();
        String [] info = new String[1];
        String [] weekdays = new String[1];
        String input = "";

        Scanner scanFile = null;
        try {
            scanFile = new Scanner(new File(filename));
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        scanFile.nextLine();//eats the first row

        while (true) {
            try {
                input = scanFile.nextLine();
            }
            catch (java.util.NoSuchElementException e) {
                break;
            }
            info = input.split(",");
            weekdays = info[2].split("/");
            for (int i = 0; i < weekdays.length; i++) {
                if (weekdays[i].equals(weekday)) {//Name,Nickname,Days,hour,minute,midi,link,password
                    String name = info[0];
                    String nickname = info[1];

                    int index = 0;
                    if (info[3].length() == 8) {//##:## xx
                        index = 1;
                    }
                    int hour = Integer.parseInt(info[3].substring(0,1+index));
                    int minute = Integer.parseInt(info[3].substring(2+index,4+index));
                    String midi = info[3].substring(5+index,7+index);

                    String link = info[4];
                    String password = info[5];

                    Class temp = new Class(name,nickname,weekdays,hour,minute,midi,link,password);
                    data.add(temp);

                }
            }
        }

        return data;
    }
    public static class Schedule {//one for each class
        private String weekday;
        private List<Class> classes;
        private List<Integer> hours;

        public Schedule(String weekday, List<Class> classes) {
            this.weekday = weekday;
            this.classes = classes;
            this.hours = updateHours();
        }

        public String getWeekday(){return weekday;}  

        public List<Class> getClasses() {return classes;}

        public List<Integer> getHours() {return hours;}

        public String getFullWeekday() {
            String str = "";
            if (weekday.equals("M")) {str = "Monday";}
            else if (weekday.equals("Tu")) {str = "Tuesday";}
            else if (weekday.equals("W")) {str = "Wednsday";}
            else if (weekday.equals("Th")) {str = "Thursday";}
            else if (weekday.equals("F")) {str = "Friday";}
            return str;
        }

        public List<Integer> updateHours() {
            List <Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < classes.size(); i++) {
                list.add(classes.get(i).getHour());
            }
            return list;
        }

        public void addClass(Class new_class) {
            classes.add(new_class);
        }

        public void info() {
            System.out.println(getFullWeekday()+": ");
            for (int i = 0; i < classes.size(); i++) {
                classes.get(i).info();
            }
            System.out.println();
        }
    }

    public static class Class {
        private String name;
        private String nickname;
        private String [] days;
        private int hour;//hour the class starts
        private int minute;//minute the class starts
        private String midi; //AM or PM. I don't know what else to call it
        private String link;
        private String password;

        public Class(String name,String nickname, String [] days, int hour, int minute, String midi, String link, String password) {
            this.name = name;
            this.nickname = nickname;
            this.days = days;
            if (midi.equals("PM") && hour != 12) {
                this.hour = hour+12;
            }
            else {
                this.hour = hour;
            }
            this.minute = minute;
            this.midi = midi;
            this.link = link;
            this.password = password;
        }

        public Class(int hour) {
            this.hour = hour;
        }

        public String getName() {return name;}//not getClass because Object has a getClass method that can't be overridden
        public String getNickname() {return nickname;}

        public String [] getDays() {return days;}

        public int getHour() {return hour;}

        public int getMinute() {return minute;}

        public String getMidi() {return midi;}

        public String getLink() {return link;}

        public String getPassword() {return password;}

        public void info() {
            System.out.print(name);
            if (!(nickname.equals("NULL"))) {
                System.out.print("('"+nickname+"')");
            }

            int thour = 0;
            if (midi.equals("PM")) {thour = hour - 12;}
            else {thour = hour;}

            String tminute = "";
            tminute += Integer.toString(minute);
            if (tminute.length() == 1) {
                tminute = "0" + tminute;
            }

            System.out.println(" at "+thour+":"+tminute+" "+midi);
        }

        public void information() {
            System.out.print(name);
            if (!(nickname.equals("NULL"))) {
                System.out.print("('"+nickname+"')");
            }

            System.out.print(" every ");
            if (days.length == 1) {
                System.out.print(days[0]);
            }
            else {
                for (int i = 0; i < days.length-2; i++) {
                    System.out.print(days[i]+", ");
                }
                System.out.print(days[days.length-2]+" and "+days[days.length-1]);
            }

            int thour = 0;
            if (midi.equals("PM")) {thour = hour - 12;}
            else {thour = hour;}

            String tminute = "";
            tminute += Integer.toString(minute);
            if (tminute.length() == 1) {
                tminute = "0" + tminute;
            }

            System.out.println(" at "+thour+":"+tminute+" "+midi);
        } 
    }

    public static class DateTime {
        private int day;
        private int month;
        private int year;
        private String weekday;
        private int hour;
        private int minute;

        public DateTime() {
            updateTime();
        }

        public void updateTime() {
            LocalDateTime currentTime = LocalDateTime.now();
            String dateTime = currentTime.toString();//yyyy-MM-ddTHH:mm:ss:ms
            //yes I know there is a '.format' method but I can't get it to work

            this.day = Integer.parseInt(dateTime.substring(8,10));  
            this.month = Integer.parseInt(dateTime.substring(5,7));    
            this.year = Integer.parseInt(dateTime.substring(0,4));    
            this.hour = Integer.parseInt(dateTime.substring(11,13));  
            this.minute = Integer.parseInt(dateTime.substring(14,16));  
            TextStyle textstyle = TextStyle.FULL; //textStyle and Locale are both arguements .getDisplayName need to converet a DayOfWeek object to a String 
            Locale murica = new Locale("English","United States of America");
            this.weekday = currentTime.getDayOfWeek().getDisplayName(textstyle,murica);
        }

        public int getDay() {return day;}

        public int getMonth() {return month;}

        public int getYear() {return year;}

        public int getHour() {return hour;}

        public int getMinute() {return minute;}

        public String getWeekday() {return weekday;}

        public String getMonthName() {
            String [] month_names = {"January","February","March","April","May","June","July","August","September","October","November","December"};
            return month_names[month-1];
        }

        public String getDate() {
            return Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year);
        }

        public String getTime() {
            if (minute < 10) {//insures format is always ##:##
                return Integer.toString(hour)+":0"+Integer.toString(minute);
            }
            return Integer.toString(hour)+":"+Integer.toString(minute);
        }

        public void printDateTime() {
            updateTime();
            System.out.println(getWeekday()+" "+getDate());
            System.out.println(getTime());
            
        }
    }

    public static void type(String text) throws Exception{
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        char letter = 'a';
        int press = 0;
        int number = -1;
        String word = "    ";
        boolean shift = false;

        for (int x = 0; x < text.length(); x++) {
            letter = text.charAt(x);
            shift = false;
            press = -1;
            word = "";

            press = letter;
            if (press >= 65 && press <= 90) {
                shift = true;
            }
            else if (press >= 97 && press <= 122) {
                press -= 32;
            }   
            else {
                press = -1;
            }

            try {
                number = Integer.parseInt(text.substring(x,x+1));
                press = number + 48;
            }
            catch (Exception e) {
                //
            }

            if (press != -1) {
                //
            }
            else if (x + 5 < text.length() && text.substring(x,x+1).equals("/") && text.substring(x+5,x+6).equals("/")) {
                word = text.substring(x+1,x+5);
                if (word.equals("BACK")) {
                    press = 8;
                }
                else if (word.equals("TAB ")) {
                    press = 9;
                }
                else if (word.equals("ENTR")) {
                    press = 10;
                }
                else if (word.equals("CAPS")) {
                    press = 20;
                }
                else if (word.equals("ESC ")) {
                    press = 27;
                }
                else if (word.equals("LEFT")) {
                    press = 37;
                }
                else if (word.equals("UP  ")) {
                    press = 38;
                }
                else if (word.equals("RGHT")) {
                    press = 39;
                }
                else if (word.equals("DOWN")) {
                    press = 40;
                }

                x += 5;

            }
            else if (letter == ' ') {
                press = 32;
            }
            else if (letter == ',') {
                press = 44;
            }
            else if (letter == '<') {
                press = 44;
                shift = true;
            }
            else if (letter == '-') {
                press = 45;
            }
            else if (letter == '_') {
                press = 45;
                shift = true;
            }
            else if (letter == '.') {
                press = 46;
            }
            else if (letter == '>') {
                press = 46;
                shift = true;
            }
            else if (letter == '/') {
                press = 47;
            }
            else if (letter == '?') {
                press = 47;
                shift = true;
            }
            else if (letter == ')') {
                press = 48;
                shift = true;
            }
            else if (letter == '!') {
                press = 49;
                shift = true;
            }
            else if (letter == '@') {
                press = 50;
                shift = true;
            }
            else if (letter == '#') {
                press = 51;
                shift = true;
            }
            else if (letter == '$') {
                press = 52;
                shift = true;
            }
            else if (letter == '%') {
                press = 53;
                shift = true;
            }
            else if (letter == '^') {
                press = 54;
                shift = true;
            }
            else if (letter == '&') {
                press = 55;
                shift = true;
            }
            else if (letter == '*') {
                press = 56;
                shift = true;
            }
            else if (letter == '(') {
                press = 57;
                shift = true;
            }
            else if (letter == ';') {
                press = 59;
            }
            else if (letter == ':') {
                press = 59;
                shift = true;
            }
            else if (letter == '=') {
                press = 61;
            }
            else if (letter == '+') {
                press = 61;
                shift = true;
            }
            else if (letter == '[') {
                press = 91;
            }
            else if (letter == '{') {
                press = 91;
                shift = true;
            }
            else if (letter == '\\') {    
                press = 92;
            }
            else if (letter == ']') {
                press = 93;
            }
            else if (letter == '}') {
                press = 93;
                shift = true;
            }
            else if (text.substring(x,x+1).equals("'")) {
                press = 222;
            }
            else if (letter == '"') {
                press = 222;
                shift = true;
            }
            else if (press == -1) {
                press = 69;
            }

            if (shift == true) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }            
            robot.keyPress(press);
            delay(5);
            robot.keyRelease(press);
            if (shift == true) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }            

        }
    }

    public static void openChrome(){
        try {
            Process p = Runtime.getRuntime().exec("\"/Program Files (x86)/Google/Chrome/Application/chrome.exe\"");
            p.waitFor();
            //System.out.println("Google Chrome launched!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delay(int x) {
        try{
            Thread.sleep(x);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void delayMin(int x) {
        x = x*60000;
        try{
            Thread.sleep(x);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

