package com.gwtapps.projects.nextCoffee.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtapps.projects.nextCoffee.client.model.Client;

public class ClientListView extends SimplePanel{
	
	private FlexTable listTable = new FlexTable();
	private NextCoffeeView view;
	
	public ClientListView( NextCoffeeView view ){
		setWidget( listTable );
		this.view = view;
		listTable.setCellPadding(0);
		listTable.setCellSpacing(0);
		addContact( view.getContactList().getMe() );
		listTable.getRowFormatter().addStyleName(0, "myContactItem" );
		listTable.getColumnFormatter().setWidth(1,"100%");
	}
	
	public void addContact( final Client client ){
		
		int row = listTable.getRowCount();
		Hyperlink link = new Hyperlink( client.getName(), client.getName() );
		listTable.getRowFormatter().setVerticalAlign(row,HasVerticalAlignment.ALIGN_TOP);
		final Image icon = new Image("icon_user.gif");
		icon.addClickListener(new ClickListener(){
			public void onClick( Widget sender ){
				try{
				RequestBuilder requestBuilder = new RequestBuilder( RequestBuilder.GET, GWT.getModuleBaseURL()+"/"+client.getName()+".html" );
				requestBuilder.sendRequest( null, new RequestCallback(){
					public void onError(Request request, Throwable exception){ /*ignored*/ }
					public void onResponseReceived(Request request, Response response){
						if( response.getStatusCode() == 200 ){
							PopupPanel popup = new PopupPanel(true);
							popup.setStyleName("popup");
							popup.setWidget( new HTML( response.getText() ));
							popup.setPopupPosition(icon.getAbsoluteLeft(), icon.getAbsoluteTop()+icon.getOffsetHeight());
							popup.show();
						}
					}

				});
				}catch( Exception e){ /*ignored*/ }
			}
		});
		listTable.setWidget(row,0,icon);
		listTable.getRowFormatter().setStyleName(row, "contactItem" );
		if( row != 0 ) listTable.setWidget(row,1,link);
		else listTable.setText(row,1, client.getName() );
	}
	
	public void removeContact( Client clinet ){
		for( int i=1; i<listTable.getRowCount();  ){
			Hyperlink link = (Hyperlink)listTable.getWidget(i,1);
			if( link.getText().compareTo( clinet.getName() ) == 0 ) listTable.removeRow(i);
			else ++i;
		}
	}

}