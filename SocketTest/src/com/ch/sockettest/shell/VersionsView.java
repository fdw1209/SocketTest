package com.ch.sockettest.shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class VersionsView extends AbsPopWindow {

	public VersionsView(Display display, int style) {
		super(display, SWT.CLOSE);
		setSize(396, 244);
		setText("¹ØÓÚÈí¼þ");
		setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, true);
		gl_composite.verticalSpacing = 1;
		gl_composite.marginWidth = 1;
		gl_composite.marginHeight = 1;
		gl_composite.marginBottom = 1;
		gl_composite.horizontalSpacing = 1;
		composite.setLayout(gl_composite);

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblNewLabel_1.heightHint = 31;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 18, SWT.NORMAL));
		GridData gd_lblNewLabel = new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 1, 1);
		gd_lblNewLabel.heightHint = 50;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setText("WIFI \u6D4B\u8BD5\u5DE5\u5177");

		Label lblV = new Label(composite, SWT.NONE);
		lblV.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 12, SWT.NORMAL));
		lblV.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblV.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblV.setText("\u7248\u672C\uFF1A V 2.5");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblc = new Label(composite, SWT.NONE);
		lblc.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		GridData gd_lblc = new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 1, 1);
		gd_lblc.heightHint = 20;
		lblc.setLayoutData(gd_lblc);
		lblc.setAlignment(SWT.CENTER);
		lblc.setText("\u957F\u8679\u7F51\u7EDC\u516C\u53F8\u7248\u6743\u6240\u6709 (c) 2018");
	}

	@Override
	protected void createContents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
