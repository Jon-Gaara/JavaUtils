package com.yumaolin.util.Test;

public enum EnumTest {
  SMALL(1),MEIUM(2),LAGRE(3),EXTRA_LARGE(4);
  private int _ncode;
  private EnumTest(int _ncode){
      this._ncode = _ncode;
  }
  
  public String toString(){
      return String.valueOf(this._ncode);
  }
}
