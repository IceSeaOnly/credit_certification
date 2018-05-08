# 基于区块链的押金证明系统(后端) ☞[前端地址](https://github.com/IceSeaOnly/cc_front)

### 演示地址
[http://cc.nanayun.cn](http://cc.nanayun.cn)

### 问题假设
假设你在购物、证明资产信用等场景中，对方需要你提供押金证明。
这个时候，你可能会担心把钱押给对方后，对方人去楼空自己无法再要回押金。

### 解决方案

该系统基于[星云链](https://nebulas.io) 智能合约，该智能合约解决了押金场景中的不信任痛点:
 用户与要求提供押金证明的机构约定好押金额度后，通过创建一条合约，即可将指定的NAS数字货币托管到该智能合约上。
 约定的押金周期结束后，用户即可自行解除合约提回数字货币。即便是对方机构倒闭不存在了，也不会影响押金的安全。

### 智能合约代码
```
'use strict';

var DepositeContent = function (text) {
  if (text) {
    var o = JSON.parse(text);
    this.val = new BigNumber(o.val);
    this.endTimeTs = new BigNumber(o.endTimeTs);
    this.uuid = o.uuid;
    this.from = o.from;
  } else {
    this.val = new BigNumber(0);
    this.endTimeTs = new BigNumber(0);
    this.uuid = 'FFFF-FFFF-FFFF-FFFF-FFFF';
    this.from = 'FFFF-FFFF-FFFF-FFFF-FFFF';
  }
};

DepositeContent.prototype = {
  toString: function () {
    return JSON.stringify(this);
  }
};

var CreditCertification = function () {
  LocalContractStorage.defineMapProperty(this, "creditCertification", {
    parse: function (text) {
      return new DepositeContent(text);
    },
    stringify: function (o) {
      return o.toString();
    }
  });
};

// save value to contract, only after height of block, users can takeout
CreditCertification.prototype = {
  init: function () {},

  apply: function (uuid,endTimeTs) {
    var from = Blockchain.transaction.from;
    var value = Blockchain.transaction.value;

    var deposit = new DepositeContent();
    deposit.val = value;
    deposit.endTimeTs = endTimeTs;
    deposit.uuid = uuid;
    deposit.from = from;

    this.creditCertification.put(uuid, deposit);
  },

  dissolution: function (uuid) {
    var deposit = this.creditCertification.get(uuid);
    if (!deposit) {
      throw new Error("deposit not exist.");
    }

    if(deposit.val <= 0){
      throw new Error("no more nas in this deposit.");
    }

    var timestamp=Blockchain.transaction.timestamp;

    if(!timestamp){
      throw new Error('can not get timestamp!');
    }

    if (timestamp*1000 < deposit.endTimeTs) {
      throw new Error("Can not dissolution to "+deposit.from +" "+ deposit.val+" before "+deposit.endTimeTs+". now is "+timestamp);
    }

    var amount = deposit.val;
    var result = Blockchain.transfer(deposit.from, amount);
    if (!result) {
      throw new Error("transfer failed.");
    }

    deposit.val = new BigNumber(0);
    this.creditCertification.put(uuid, deposit);

    return "dissolution success. transfered "+amount+" to "+deposit.from+", now is "+timestamp+",result:"+result;
  },

  watchContract: function (uuid) {
    return this.creditCertification.get(uuid);
  }
};
module.exports = CreditCertification;

```

### 系统优点

本系统免费开放注册使用，任何机构和个人都可以注册对应账户进行合约创建和管理，数据透明，合约一旦生效，无法二次更改。