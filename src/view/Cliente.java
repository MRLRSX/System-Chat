package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Cliente extends JFrame {

	public JTextField textoParaEnviar;
	private Socket socket;
	private PrintWriter printWriter;
    private String nome;
    private JTextArea textoServidor;
    Scanner scanner;
    
    private class EscutaServidor implements Runnable{

		@Override
		public void run() {
			try {
			String texto;
			while((texto = scanner.nextLine()) != null) {
				System.out.println(texto);
				textoServidor.append(texto + "\n");
			}
			}catch(Exception error) {
				error.printStackTrace();
			}
		}
    	
    }
    
    
	public Cliente(String nome) {
        
		setTitle("CHAT " + nome);
        this.nome = nome;
		Font fonte = new Font("Serif", Font.PLAIN, 26);

		textoParaEnviar = new JTextField();
		JButton botao = new JButton("Enviar");
		botao.setFont(fonte);
		botao.setForeground(Color.blue);
        botao.addActionListener(new EnviarListener());
		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, textoParaEnviar);
		envio.add(BorderLayout.EAST, botao);

		textoServidor = new JTextArea();
		textoServidor.setFont(fonte);
		JScrollPane scroll = new JScrollPane(textoServidor);

		getContentPane().add(BorderLayout.CENTER, scroll);
		getContentPane().add(BorderLayout.SOUTH, envio);
		
        configurarRede();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);
	}

	public void configurarRede() {

		try {
			socket = new Socket("127.0.0.1", 5000);
			printWriter = new PrintWriter(socket.getOutputStream());
			scanner = new Scanner(socket.getInputStream());
			new Thread(new EscutaServidor()).start();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Cliente("LUCAS");
		new Cliente("ANNA CLARA");
	}
	
	private class EnviarListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			printWriter.println(nome + " : " + textoParaEnviar.getText());
			printWriter.flush();
			textoParaEnviar.setText("");
			textoParaEnviar.requestFocus();
		}
		
	}
}
