package com.umpay.ethrpc4j;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.ethrpc4j.methods.DbMethods;
import com.umpay.ethrpc4j.methods.EthMethods;
import com.umpay.ethrpc4j.methods.PersonalMethods;
import com.umpay.ethrpc4j.util.Util;

public class DeployContract {
	private static final Logger _log = LoggerFactory.getLogger(DeployContract.class);
	
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://10.10.67.217:8545");
		
		final EthereumClient client2 = EthereumClient.getInstanceByURL(url);
		final EthMethods eth2 = client2.eth();
		
		
		final EthereumClient client = EthereumClient.getDefaultInstance();
		final EthMethods eth = client.eth();
		final DbMethods db = client.db();
		final PersonalMethods personal = client.personal();
		String myAccount = "";
		String myAccount2 = "0x403c9ddf6c1c30ba8f5cea7e7585cf69a85d0cc0";
		List<String> accounts = eth.accounts();
		if(accounts.size() > 0){
			myAccount = accounts.get(0);
		}
		_log.info("\n Compile Contract \n");
		String contract = Util.readString(new File("D:\\lab\\test.sol"));
		String contractName = "Token";
		Map<String,Object> map = eth.compileSolidity(contract);
		String code = ((Map<String,Object>)map.get(contractName)).get("code").toString();
		Map<String,Object> info = (Map<String,Object>)((Map<String,Object>)map.get(contractName)).get("info");
		String abiDefinition = info.get("abiDefinition").toString();
		_log.info("\n###Contract Code\n" + code);
		_log.info("\n###Contract Info\n" + info);
		_log.info("\n###Contract abiDefinition:\n" + abiDefinition);
		_log.info("\n\n\n");
		
		_log.info("\nDeploy Contract\n");
		Map<String,String> parma = new HashMap<String,String>();
		parma.put("from", myAccount2);
		parma.put("gas","0x" + Long.toHexString(500000));
		parma.put("gasPrice", "0x" +Long.toHexString(300));
		parma.put("data", code);
		String thash = eth2.sendTransaction(parma);
		_log.info("#TransactionHash:"+thash);
		int times = 0;
		long preBlockNumber = eth.blockNumber();
		while(times < 60){
			long bn = eth.blockNumber();
			if(bn > preBlockNumber){
				break;
			}else{
				Thread.sleep(1000);
			}
			times++;
		}
		Thread.sleep(30000);
		_log.info("\ngetTransactionReceipt\n");
		times = 0;
		String contractAddress = null;
		while(times < 60){
			try {
				Map<String,Object> map2 = eth.getTransactionReceipt(thash);
				contractAddress = map2.get("contractAddress").toString();
				_log.info("###ContractAddress\n" + map2.get("contractAddress"));
				break;
			} catch (Exception e) {
				continue;
			}
		}
		
		Map<String,String> myp = new HashMap<String,String>();
		myp.put("from", myAccount);
		myp.put("to", contractAddress);
		myp.put("gas","0x" + Long.toHexString(60000));
		myp.put("gasPrice", "0x" +Long.toHexString(900));
		myp.put("data", "0xc6888fa10000000000000000000000000000000000000000000000000000000000000007");
		String rhash = eth.sendTransaction(myp);
		_log.info("rhash:\n" + rhash);
		
		
		//LOG.info(eth.getTransactionReceipt(thash).toString());
	}
}
