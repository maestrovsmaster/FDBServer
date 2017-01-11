package Connect;

public class ConnectDBFactory /*extends ConnectsFactory*/{


	/*public Firebird createFirebird(String ip, String pathdb, String login, String pass, String charset) {
		
		return new ConnectFirebird("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql:" + ip + "/3050:" +pathdb,  login, pass,  charset);
	}*/
	
	private ConnectDBFactory(){
		
	};
	
	private static ConnectFirebird  connectFirebird = null;

	public static Firebird getInstanceFirebird(String ip, String pathdb) {
		if(connectFirebird==null){
			//System.out.println("connect firebird is null!!!");
	
			connectFirebird = ConnectFirebird.getConnectFirebird("org.firebirdsql.jdbc.FBDriver","jdbc:firebirdsql:" + ip + "/3050:" +pathdb,  "SYSDBA", "masterkey",  "UNICODE_FSS");
		}
	
	return connectFirebird ;
	
	}
	
	public static void deleteConnectFirebird()
	{
		if(connectFirebird!=null) connectFirebird=null;
	}

	
	

}
