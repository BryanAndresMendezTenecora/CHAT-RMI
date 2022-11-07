package cliente;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import server.InterfazRemoto;




public class Cliente extends UnicastRemoteObject implements InterfazCliente {
	private static final long serialVersionUID = 7468891722773409712L;
	Ventana gui;
	private String hostName = "localhost";
	private String serviceName = "GroupChatService";
	private String clientServiceName;
	private String name;
	protected InterfazRemoto serverIF;
	protected boolean connectionProblem = false;
	
	public Cliente(Ventana gui, String userName) throws RemoteException {
		super();
		this.gui = gui;
		this.name = userName;
		this.clientServiceName = "ClientListenService_" + userName;
	}
	
	public void startClient() throws RemoteException {		
		String[] details = {name, hostName, clientServiceName};	

		try {
			Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
			serverIF = ( InterfazRemoto )Naming.lookup("rmi://" + hostName + "/" + serviceName);	
		} 
		catch (ConnectException  e) {
			JOptionPane.showMessageDialog(
					gui.frame, "El servidor parece no estar disponible \\ Int�ntelo m�s tarde",
					"Connection problem", JOptionPane.ERROR_MESSAGE);
			connectionProblem = true;
			e.printStackTrace();
		}
		catch(NotBoundException | MalformedURLException me){
			connectionProblem = true;
			me.printStackTrace();
		}
		if(!connectionProblem){
			registerWithServer(details);
		}	
		System.out.println("RMI --- CONECTADO");
	}
	
	public void registerWithServer(String[] details) {		
		try{
			serverIF.pasoIDentity(this.ref);//now redundant ??
			serverIF.registrarListener(details);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void messageFromServer(String message) throws RemoteException {
		System.out.println( message );
		gui.textArea.append( message );
		//make the gui display the last appended text, ie scroll to bottom
		gui.textArea.setCaretPosition(gui.textArea.getDocument().getLength());
	}

	@Override
	public void updateUserList(String[] currentUsers) throws RemoteException {
		
		gui.userPanel.remove(gui.clientPanel);
		gui.setClientPanel(currentUsers);
		gui.clientPanel.repaint();
		gui.clientPanel.revalidate();
	}
}
