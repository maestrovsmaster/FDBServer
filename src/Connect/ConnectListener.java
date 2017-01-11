package Connect;

import java.util.ArrayList;

import javax.swing.table.TableModel;

public interface ConnectListener {

	public void setSelectedRow(ArrayList<Object> row);
	
	public void addConnectListener(ConnectListener listener);
	
}
