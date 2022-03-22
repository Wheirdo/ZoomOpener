import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
public class ZoomOpen{
    public static void main() {
        Scanner scan = new Scanner(System.in);
        String input = "";
        String URL = "";
        String password = "";
        List <String []> data = new ArrayList<String []>();
        data = readFile();
        scan.nextLine();

        
        System.out.println("What class would you like to open?");   
        input = scan.nextLine();
        for (int i = 0; i < data.size(); i++){ 
            if (input.equals(data.get(i)[0])) {
                URL = data.get(i)[4];
                password = data.get(i)[4];

                openChrome();
                delay(2000);
                try {
                    type(URL);
                    type("/ENTR/");
                    delay(3000);
                    type(password);
                    type("/ENTR/");
                }
                catch (Exception e) {
                    //
                }
                break;
            }
        }

        
        delay(2000);
        System.exit(0);
    }

    public static void openChrome(){
        try {
            Process p = Runtime.getRuntime().exec("\"/Program Files (x86)/Google/Chrome/Application/chrome.exe\"");
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String []> readFile() {
        List <String []> data = new ArrayList<String []>();
        String input = "";
        String [] info = new String[2];

        Scanner scanFile = null;
        try {
            scanFile = new Scanner(new File("Fall2020.csv"));
        }
        catch (Exception err) {
            err.printStackTrace();
        }

        while (true) {
            try {
                input = scanFile.nextLine();
            }
            catch (java.util.NoSuchElementException e) {
                break;
            }

            info = input.split(",");
            data.add(info);
        }

        return data;
    }

    public static void type(String text) throws Exception{
        Robot robot = new Robot();
        robot.setAutoDelay(25);
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

    public static void delay(int x) {
        try{
            Thread.sleep(x);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}