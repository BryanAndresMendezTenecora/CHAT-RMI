package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public interface InterfazRemoto extends Remote {

public void actualizarChat(String userName, String chatMessage)throws RemoteException;
	
	public void pasoIDentity(RemoteRef ref)throws RemoteException;
	
	public void registrarListener(String[] details)throws RemoteException;
	
	public void salirChat(String userName)throws RemoteException;
	
	public void enviarPM(int[] privateGroup, String privateMessage)throws RemoteException;
}
