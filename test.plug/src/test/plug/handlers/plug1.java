package test.plug.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

public class plug1 extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		
		MessageDialog.openInformation(null, "���ǲ����Ŀ1 ����ʾ", "��ϲ�������˲��1 ��Ŀ");
		return null;
	}

}
