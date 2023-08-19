
package gerenciabanco;

import java.util.Scanner;

public class GerenciaBanco {

    public static boolean validarCPF(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "");

        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false;
        }

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += digits[i] * (10 - i);
        }

        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) {
            firstDigit = 0;
        }

        if (digits[9] != firstDigit) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * (11 - i);
        }

        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) {
            secondDigit = 0;
        }

        return digits[10] == secondDigit;
    }
    
    public static void main(String[] args) {
        //Criado para armazenar o histórico da conta.
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Bem-vindo ao Gerenciamento Bancario!");
            
            //Iniciando o histórico
            sb.append("\n----Historico:----");
            
            System.out.print("Digite seu nome: ");
            String nome = scanner.nextLine();
            
            System.out.print("Digite seu sobrenome: ");
            String sobrenome = scanner.nextLine();
            
            //Salvando ação no histórico.
            sb.append(String.format("\nNome: %s %s",nome, sobrenome));
            
            System.out.print("Digite seu CPF: ");
            String cpf = scanner.nextLine();
            
            //executa que um cpf válido seja informado.
            while (!validarCPF(cpf)) {
                System.out.print("Digite um CPF valido: ");
                cpf = scanner.nextLine();
                sb.append("\n----Erro: Digitou CPF inválido.----");
            }
            
            //Salvando ação no histórico.
            sb.append(String.format("\nCPF: %s.%s.%s-%s",
                    cpf.substring(0, 3),
                    cpf.substring(3, 6),
                    cpf.substring(6, 9),
                    cpf.substring(9)));
            
            
            ContaBancaria conta = new ContaBancaria(nome, sobrenome, cpf);
            
            boolean continuar = true;
            
            //Executa até a opção de sair for escolhida.
            while (continuar) {
                System.out.println("\nEscolha uma opcao:");
                System.out.println("1 - Consultar saldo");
                System.out.println("2 - Realizar deposito");
                System.out.println("3 - Realizar saque");
                System.out.println("4 - Ver Historico");
                System.out.println("5 - Encerrar");
                
                int opcao = scanner.nextInt();
                
                switch (opcao) {
                    case 1 -> {
                        System.out.print("\033[H\033[2J"); //Limpando o terminal despoluir a saída de dados.
                        System.out.flush();
                        System.out.println("Seu saldo atual e: R$ " + conta.consultarSaldo());
                        
                        //Salvando ação no histórico.
                        sb.append(String.format("\nConsultou o saldo. era: %s",conta.consultarSaldo()));
                    }
                    
                    case 2 -> {
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.print("Digite o valor do deposito: ");
                        try{
                            
                            double valorDeposito = Double.parseDouble(scanner.next().replace(',', '.'));
                            conta.depositar(valorDeposito);
                            
                            //Salvando ação no histórico.
                            sb.append(String.format("\nDepositou: %s",valorDeposito));
                        }
                        catch(Exception e){
                            System.out.println("Erro ao depositar.");
                            
                            //Salvando ação no histórico.
                            sb.append("\n----Erro ao depositar.----");
                        }
                    }
                    case 3 -> {
                        System.out.print("\033[H\033[2J"); //Limpando o terminal despoluir a saída de dados.
                        System.out.flush();
                        System.out.print("Digite o valor do saque: ");
                        try{
                            
                            double valorSaque = Double.parseDouble(scanner.next().replace(',', '.'));
                            boolean sacou = conta.sacar(valorSaque);
                            
                            //Salvando ação no histórico.
                            if(sacou)
                                sb.append(String.format("\nSacou: %s",valorSaque));
                            else
                                sb.append("\n----Erro ao sacar. Saldo insuficiente.----");
                            
                        }catch(NumberFormatException e){
                            System.out.println("Erro ao sacar.");
                            
                            //Salvando ação no histórico.
                            sb.append("\n----Erro ao sacar.----");
                        }
                    }
                    
                    case 4 -> {
                        System.out.print("\033[H\033[2J"); //Limpando o terminal despoluir a saída de dados.
                        System.out.flush();
                        
                        //Imprimindo o histórico da conta.
                        System.out.println(sb.toString());
                    }
                    
                    case 5 -> {
                        continuar = false;
                        System.out.print("\033[H\033[2J"); //Limpando o terminal despoluir a saída de dados.
                        System.out.flush();
                        
                        //Mensagem de saída.
                        System.out.println("Obrigado por utilizar o Gerenciamento Bancario!");
                    }
                    
                    default -> {
                        System.out.print("\033[H\033[2J"); //Limpando o terminal despoluir a saída de dados.
                        System.out.flush();
                        System.out.println("Opcao invalida.");
                    }
                }
            }
        }
    }
    
}
