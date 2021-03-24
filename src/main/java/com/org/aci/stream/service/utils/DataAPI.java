package com.org.aci.stream.service.utils;

import com.org.aci.stream.service.constants.AppConstant;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataAPI {
    /**
     * This method is used to compare two JSON Data sets
     * @param srcAry Expected result sets from user
     * @param dscAry Actual result set which we got it from Hive
     * @return Result Array
     */
    public static JSONArray compare2Ds(JSONArray srcAry,JSONArray dscAry){
        JSONArray rsAry = new JSONArray();

        for(int i = 0; i < srcAry.length(); i++){

            JSONObject srcObj =  srcAry.getJSONObject(i);
            JSONObject dscObj =  dscAry.getJSONObject(i);

            for(String srcKey :  srcObj.keySet()){
                JSONObject rsObj =  new JSONObject();

                if(dscObj.has(srcKey)){
                    rsObj.put("field_name",srcKey);
                    if(dscObj.get(srcKey).toString().equals(srcObj.get(srcKey).toString())){
                        rsObj.put(AppConstant.DESC,AppConstant.MATCHED);
                    }else{
                        rsObj.put(AppConstant.DESC,AppConstant.UNMATCHED+"-val[exp->act]="+srcObj.get(srcKey).toString()+"-"+dscObj.get(srcKey).toString());
                    }
                }else{
                    rsObj.put(AppConstant.DESC,AppConstant.UNMATCHED+"-field[exp]=" +srcKey);
                }
                rsAry.put(rsObj);
            }


        }
        return rsAry;
    }
}
