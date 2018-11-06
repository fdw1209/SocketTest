package com.ch.sockettest.shell;

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 抽象窗体类
 * 
 * @author yuanji
 * 
 */
public abstract class AbsPopWindow extends Shell {
	protected Shell parent;
	private static Image DEFAULT_ICON = SWTResourceManager.getImage(MainView.class, "/icon/tray.png");

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public void open() {
		try {
			if (parent != null && !parent.isDisposed()) {
				parent.setEnabled(false);// false表示父窗口不能被点击;父窗体存在且要处于打开状态即代码为：parent
											// != null && !parent.isDisposed();
			}

			createContents();
			//center();
			Monitor monitor = this.getMonitor();
			Rectangle bounds = monitor.getBounds();
			Rectangle rect = this.getBounds();
			int x = bounds.x + (bounds.width - rect.width) / 2;
			int y = bounds.y + (bounds.height - rect.height) / 2;
			this.setLocation(x, y);
			
			ansyInitData();
			super.open();
			super.layout();
			while (!super.isDisposed()) {
				if (!getDisplay().readAndDispatch()) {
					getDisplay().sleep();
				}
			}
			if (parent != null) {
				parent.setEnabled(true);
				TimeUnit.MILLISECONDS.sleep(5);
				parent.setActive();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public AbsPopWindow(Display display, Shell parent) {
		super(display, SWT.SHELL_TRIM);
		this.parent = parent;

		this.setImage(DEFAULT_ICON);
	}

	public AbsPopWindow(Display display, Shell parent, int style) {
		super(display, style);
		this.parent = parent;

		this.setImage(DEFAULT_ICON);
	}

	public AbsPopWindow(Display display, int style) {
		super(display, style);

		this.setImage(DEFAULT_ICON);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public AbsPopWindow(Display display) {
		super(display, SWT.SHELL_TRIM);
		this.parent = null;

		this.setImage(DEFAULT_ICON);
	}

	/**
	 * Create contents of the shell.
	 */
	protected abstract void createContents();

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public abstract void initData();

//    public void center() {
//		Monitor monitor = this.getMonitor();
//		Rectangle bounds = monitor.getBounds();
//		Rectangle rect = this.getBounds();
//		int x = bounds.x + (bounds.width - rect.width) / 2;
//		int y = bounds.y + (bounds.height - rect.height) / 2;
//		this.setLocation(x, y);
//	}

	protected Shell getParentShell() {
		return parent;
	}

	protected void setParentShell(Shell parent) {
		this.parent = parent;
	}

	private void ansyInitData() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initData();
			}
		});
	}
}
