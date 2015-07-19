package com.client.utils;

import java.util.ArrayList;

import com.client.servermanager.ServerManager;

public class ClientSideUtils {

	public static void GetScannedDevicesFromSharedPreferences(
			ArrayList<String> servers) {
		if (servers != null) {

			String[] splitserver = null;
			String name = "";
			String ip = "";
			String port = "";

			// more than one server (device) scanned
			if (servers.get(0).contains(" ")) {
				// each server
				splitserver = servers.get(0).split(" ");

				for (String foundServer : splitserver) {
					if (!foundServer.equals("")) {
						String[] serverParams = foundServer.split(",");

						name = serverParams[0].contains("[") ? serverParams[0]
								.substring(1) : serverParams[0];
						ip = serverParams[1];
						port = serverParams[2].contains("]") ? serverParams[2]
  								.substring(0, serverParams[2].length() - 1)
								: serverParams[2];

						ServerManager.getInstance().addServerFromCode(
								ip + "," + port + "," + name);
					}
				}

			} else {
				// there is only one in the memory
				String[] serverParams = servers.get(0).split(",");


				name = serverParams[0].contains("[") ? serverParams[0]
						.substring(1) : serverParams[0];
				ip = serverParams[1];
				port = serverParams[2].contains("]") ? serverParams[2]
							.substring(0, serverParams[2].length() - 1)
						: serverParams[2];
							
				ServerManager.getInstance().addServerFromCode(
						ip + "," + port + "," + name);

			}
		}
	}
}
