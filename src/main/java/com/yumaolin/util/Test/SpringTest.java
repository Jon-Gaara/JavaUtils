package com.yumaolin.util.Test;

public class SpringTest {
	   public static int maxProduct(int[] nums) {
	        if(nums==null){
	            return 0;
	        }
	        if(nums.length<2){
	            return nums[0];
	        }
	        int sum = 0;
	        for (int i = 0;i < nums.length-1;i++) {
	                if (nums[i] == nums[i+1]+1 || nums[i] == nums[i+1]-1) {
	                    if (sum <= (nums[i] * nums[i+1]) ) {
	                        sum = (nums[i] * nums[i+1])==0?nums[i]>nums[i+1]?nums[i]:nums[i+1]:(nums[i] * nums[i+1]);
	                        System.out.println(sum);
	                        break;
	                    }
	                }else{
	                    int count = (nums[i] * nums[i+1])<0 ? (nums[i] * nums[i+1])*-1 : (nums[i] * nums[i+1]);
	                    if (sum < count ) {
	                        sum = count;
	                    }
	                }
	        }
	        return sum;
	    }
	public static void main(String[] args) {
		System.out.println(SpringTest.maxProduct(new int[]{-3,0,1,-2}));
	}
}
