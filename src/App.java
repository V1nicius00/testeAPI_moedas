import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        
        DecimalFormat df = new DecimalFormat("#.00");

        Scanner scan = new Scanner(System.in);

        int control = 1;
        
        while( control != 0){

            System.out.println("Digite a sigla da moeda que deseja fazer a conversão para Real (USD, GBP, EUR...): ");
            String moeda = scan.nextLine().toUpperCase();

            System.out.println("Digite o valor que deseja converter: ");
            double valor = scan.nextDouble();
            scan.nextLine();
    
            try {
                // URL da API
                String apiUrl = "https://economia.awesomeapi.com.br/last/" + moeda + "-BRL";
    
                // Criar uma conexão HTTP
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
    
                // Ler a resposta da API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
    
                // Fechar a conexão
                connection.disconnect();
    
                String jsonResponse = response.toString();
    
                int indexOfBid = jsonResponse.indexOf("\"bid\":\"");
                if (indexOfBid != -1) {
                    int startIndex = indexOfBid + 7;
                    int endIndex = jsonResponse.indexOf("\"", startIndex);
                    if (endIndex != -1) {
                        String bid = jsonResponse.substring(startIndex, endIndex);
                        double bidNumber = Double.parseDouble(bid);
                        double total = valor * bidNumber;
                        System.out.println("Cotação " + moeda + ": " + df.format(bidNumber) + "\nValor convertido: "  + "R$ " + df.format(total) + "\n");
                    }
                } else {
                    System.out.println("Cotação não encontrada na resposta da API.\n");
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Deseja fazer outra conversão? \nSim - 1\nNão - 0;");
            control = scan.nextInt();
            scan.nextLine();
        }

        System.out.println("Programa finalizado.");

        scan.close();
    }
}
