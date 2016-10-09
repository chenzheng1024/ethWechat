//Sample contract
contract wechat
{
	mapping(uint256 =>  string[]) tm;
	mapping(uint256 => uint) balance;
	uint last;
	string lasts;
	
	function setString(uint256 mobile,string s){
		tm[mobile].push(s);
	}
	function getString(uint256 mobile,uint index) constant returns (string){
		return tm[mobile][index];
	}
	function getStringLength(uint256 mobile) constant returns (uint) {
		return tm[mobile].length;
	}
	
	function register(uint256 mobile){
		if(balance[mobile] != 0){
			return;
		}
		last=mobile;
		balance[mobile]=10;
		string memory x = strConcat("+",":","10",":","INIT");
		tm[mobile].push(x);
	}
	function transfer(uint256 src,uint256 target,uint amount,string amounts){
		if(balance[src] == 0){
			return;
		}
		if(balance[target] == 0){
			return;
		}
		if(balance[src] < amount){
			return;
		}
		balance[src] -= amount;
		balance[target] += amount;
		string memory detail_src=strConcat("-",":",amounts,":",uintToString(target));
		string memory detail_t=strConcat("+",":",amounts,":",uintToString(src));
		lasts = detail_src;
		tm[src].push(detail_src);
		tm[target].push(detail_t);
	}
	function getBlance(uint256 mobile) constant returns (uint){
		//return + balance[mobile];;
		return balance[mobile];
	}
	function getDetail(uint256 mobile,uint i) constant  returns (string){
		return tm[mobile][i];
	}
	
function strConcat(string _a, string _b, string _c, string _d, string _e) internal returns (string){
        bytes memory _ba = bytes(_a);
        bytes memory _bb = bytes(_b);
        bytes memory _bc = bytes(_c);
        bytes memory _bd = bytes(_d);
        bytes memory _be = bytes(_e);
        string memory abcde = new string(_ba.length + _bb.length + _bc.length + _bd.length + _be.length);
        bytes memory babcde = bytes(abcde);
        uint k = 0;
        for (uint i = 0; i < _ba.length; i++) babcde[k++] = _ba[i];
        for (i = 0; i < _bb.length; i++) babcde[k++] = _bb[i];
        for (i = 0; i < _bc.length; i++) babcde[k++] = _bc[i];
        for (i = 0; i < _bd.length; i++) babcde[k++] = _bd[i];
        for (i = 0; i < _be.length; i++) babcde[k++] = _be[i];
        return string(babcde);
}
	function uintToBytes(uint v) constant returns (bytes32 ret) {
    if (v == 0) {
        ret = '0';
    }
    else {
        while (v > 0) {
            ret = bytes32(uint(ret) / (2 ** 8));
            ret |= bytes32(((v % 10) + 48) * 2 ** (8 * 31));
            v /= 10;
        }
    }
    return ret;
}
	function bytes32ToString (bytes32 data) constant returns (string) {
    bytes memory bytesString = new bytes(32);
    for (uint j=0; j<32; j++) {
        byte char = byte(bytes32(uint(data) * 2 ** (8 * j)));
        if (char != 0) {
            bytesString[j] = char;
        }
    }
    return string(bytesString);
}
	function uintToString(uint x) constant returns (string) {
		return bytes32ToString(uintToBytes(x));
	}
	
	
}
