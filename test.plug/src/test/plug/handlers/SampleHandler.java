package test.plug.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import test.java.test1;

import org.eclipse.jface.dialogs.MessageDialog;

public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		
		MessageDialog.openInformation(null, "��ʾ", test1.test1("�����Ŀ��"));
		System.out.println(test1.test1("�����Ŀ��"));
		
		MessageDialog.openInformation(
				window.getShell(),
				"Plug",
				"���ٲ����");
		
		
		return null;
	}
}
