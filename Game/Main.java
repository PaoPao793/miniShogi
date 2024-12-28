package Game;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("-i")) { // interactive mode 
            new Game(true, "");
        } else if (args.length == 2 && args[0].equals("-f")) { // file mode 
            if (!args[0].equals("-f")) {
                System.out.println("Error in command line args");
                return;
            }
            String filepath = args[1];
            new Game(false, filepath);
        } else { // incorrect input 
            System.out.println("Please specify input by -i or -f [file name]");
        }
    }
}