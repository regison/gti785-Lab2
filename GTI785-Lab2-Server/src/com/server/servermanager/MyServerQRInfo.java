package com.server.servermanager;

import android.graphics.Bitmap;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MyServerQRInfo {

	private String name;
	private String address;
	private int port;

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;
	
	public MyServerQRInfo(int port){
		this.port = port;
	}

	public String getAddress(){
		return getCurrentIpAddress().get(0);
	}


	public Bitmap generateQRCode(){
		try {
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

			QRCodeWriter qrCodeWriter = new QRCodeWriter();

			BitMatrix byteMatrix = qrCodeWriter.encode("MonServeur ,"+ getAddress() +","+ port,BarcodeFormat.QR_CODE, 125, 125, hintMap);
			
			//Comme c un carré on peut juste la longueur
			int CrunchifyWidth = byteMatrix.getWidth();

			int[] pixels = new int[CrunchifyWidth * CrunchifyWidth];
			// All are 0, or black, by default
			for (int y = 0; y < CrunchifyWidth; y++) {
				int offset = y * CrunchifyWidth;
				for (int x = 0; x <CrunchifyWidth; x++) {
					if(byteMatrix.get(x, y)== true)
					{
						pixels[offset + x] = BLACK ;
					}
					else
						pixels[offset + x] = WHITE ;
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(CrunchifyWidth, CrunchifyWidth, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, CrunchifyWidth, 0, 0, CrunchifyWidth, CrunchifyWidth);
			
			return bitmap;
			
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * return an array of ip adress from the system
	 * @return
	 */
	private ArrayList<String> getCurrentIpAddress() {
		String ip = "";

		ArrayList<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();


					if (inetAddress.isSiteLocalAddress()) {

						//Response reponse = server.serve(inetAddress)
						ipList.add(inetAddress.getHostAddress());
					}

				}

			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}

		
		return ipList;
	}

}
