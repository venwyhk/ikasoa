/*
 * Ikasoa RPC JavaScript Client  (http://ikasoa.com/)
 * 
 * @license MIT License (c) copyright 2018 original author or authors.
 * @author Larry
 * @version 1.0
 */

JavaType = {
  _byte: { value: "byte", array: 0 },
  Byte: { value: "java.lang.Byte", array: 0 },
  _int: { value: "int", array: 0 },
  Integer: { value: 'java.lang.Integer', array: 0 },
  _long: { value: "long", array: 0 },
  Long: { value: "java.lang.Long", array: 0 },
  String: { value: 'java.lang.String', array: 0 },
  _float: { value: "float", array: 0 },
  Float: { value: "java.lang.Float", array: 0 },
  _double: { value: "double", array: 0 },
  Double: { value: "java.lang.Double", array: 0 },
  _boolean: { value: "boolean", array: 0 },
  Boolean: { value: "java.lang.Boolean", array: 0 },
  _char: { value: "char", array: 0 },
  Char: { value: "java.lang.Character", array: 0 },
  String_: { value: "[Ljava.lang.String;", array: 1 },
  _byte_: { value: "[B", array: 1 },
  Byte_: { value: "[Ljava.lang.Byte;", array: 1 },
  _int_: { value: "[I", array: 1 },
  Integer_: { value: "[Ljava.lang.Integer;", array: 1 },
  _long_: { value: "[L", array: 1 },
  Long_: { value: "[Ljava.lang.Long;", array: 1 },
  _float_: { value: "[F", array: 1 },
  Float_: { value: "[Ljava.lang.Float;", array: 1 },
  _double_: { value: "[D", array: 1 },
  Double_: { value: "[Ljava.lang.Double;", array: 1 },
  _boolean_: { value: "[Z", array: 1 },
  Boolean_: { value: "[Ljava.lang.Boolean;", array: 1 },
  List: { value: "java.util.List", array: 1 },
  Map: { value: "java.util.Map", array: 1 },
  Set: { value: "java.util.Set", array: 1 }
};

_JavaType = function(value, array) {
  this.value = value;
  this.array = array;
};

function getService(sUrl, cName, mName, rType, pTypes) {
  var tMp = new Thrift.Multiplexer();
  var tTp = new Thrift.Transport(sUrl);
  var rTypeStr = "void";
  var pTypesStr = "[]";
  if(rType !== null) rTypeStr = rType.value;
  if(pTypes !== null) {
    if(!(pTypes instanceof Array)) pTypes = new Array(pTypes);
    if (pTypes.length > 0) {
      pTypesStr = "[";
      for(i = 0; i < pTypes.length; i++) {
        var prefix = suffix = '"';
        if(pTypes.length != i + 1) suffix += ',';
        pTypesStr += prefix + pTypes[i].value + suffix;
      }
      pTypesStr += "]";
    }
  }
  return new Service(tMp.createClient(('{"iClass"|"' + cName + '","methodName"|"' + mName + '","parameterTypes"|' + pTypesStr + ',"returnType"|"' + rTypeStr + '"}').replace(/\"/g, '\\"'), ServiceClient, tTp), pTypes, pTypesStr, rType);
};

Service = function(tService, pTypes, pTypesStr, rType) {
  this.tService = tService;
  this.pTypes = pTypes;
  this.pTypesStr = pTypesStr;
  this.rType = rType;
}

Service.prototype.execute = function() {
  var argsStr = "";
  if (arguments.length > 0) {
    if(arguments.length < this.pTypes.length) throw "Arguments length error !";
    argsStr = String.fromCharCode(150) + "[";
    for(i = 0; i < this.pTypes.length; i++) {
      argsStr += JSON.stringify(arguments[i]);
      if(arguments.length != i + 1) argsStr += ',';
    }
    argsStr += "]";
  }
  var r = this.tService.get(this.pTypesStr + argsStr).split(String.fromCharCode(150));
  if(r.length == 1)
    if(r[0] == String.fromCharCode(152)) return;
    else throw JSON.parse(r[0]);
  if(this.rType.array) return JSON.parse(r[1]);
  return r[1];
};

ServiceGetArgs = function(args) {
  this.arg = null;
  if (args && args.arg !== undefined && args.arg !== null) this.arg = args.arg;
};
ServiceGetArgs.prototype = {};
ServiceGetArgs.prototype.write = function(output) {
  output.writeStructBegin("Service_get_args");
  if (this.arg !== null && this.arg !== undefined) {
    output.writeFieldBegin("arg", Thrift.Type.STRING, 1);
    output.writeString(this.arg);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

ServiceGetResult = function(args) {
  this.success = null;
  if (args && args.success !== undefined && args.success !== null) this.success = args.success;
};
ServiceGetResult.prototype = {};
ServiceGetResult.prototype.read = function(input) {
  input.readStructBegin();
  while(true) {
    var ret = input.readFieldBegin();
    if(ret.ftype == Thrift.Type.STOP) break;
    if(ret.fid == 0) this.success = input.readString().value;
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

ServiceGetResult.prototype.write = function(output) {
  output.writeStructBegin("Service_get_result");
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin("success", Thrift.Type.STRING, 0);
    output.writeString(this.success);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

ServiceClient = function(input, output) {
    this.input = input;
    this.output = (!output) ? input : output;
    this.seqid = 0;
};
ServiceClient.prototype = {};
ServiceClient.prototype.get = function(arg) {
  this.sendGet(arg);
  return this.recvGet();
};

ServiceClient.prototype.sendGet = function(arg) {
  this.output.writeMessageBegin("get", Thrift.MessageType.CALL, this.seqid);
  var params = {
    arg: arg
  };
  var args = new ServiceGetArgs(params);
  args.write(this.output);
  this.output.writeMessageEnd();
  return this.output.getTransport().flush();
};

ServiceClient.prototype.recvGet = function() {
  this.input.readMessageBegin();
  var result = new ServiceGetResult();
  result.read(this.input);
  this.input.readMessageEnd();
  if (null !== result.success) return result.success;
  throw "get failed: unknown result";
};
