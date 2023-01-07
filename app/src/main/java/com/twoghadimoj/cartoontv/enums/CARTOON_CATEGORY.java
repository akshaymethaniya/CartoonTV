package com.twoghadimoj.cartoontv.enums;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.twoghadimoj.cartoontv.R;

import java.util.HashMap;
import java.util.Map;

public enum CARTOON_CATEGORY {
    OGGY_AND_COCKRACHES("OGGY", R.drawable.oggy),
    MR_BEAN("MR_BEAN",R.drawable.mrbean),
    CHHOTA_BHEEM("CHHOTA_BHEEM",R.drawable.chhotabheem),
    BEN_10("BEN_10",R.drawable.ben10),
    MOTU_PATLU("MOTU_PATLU",R.drawable.motu_patlu),
    POKEMON("POKEMON",R.drawable.pokemon),
    TOM_AND_JERRY("TOM_AND_JERRY",R.drawable.tomandjerry),
    POWER_RANGERS_SPD("POWER_RANGERS_SPD",R.drawable.powerrangerspd),
    BANDBUDH_AUR_BUDBAK("BANDBUDH_AUR_BUDBAK",R.drawable.bandbudhaurbudbaklogo),
    MIGHTY_RAJU("MIGHTY_RAJU",R.drawable.mightyraju),
    SCOOBY_DOO("SCOOBY_DOO",R.drawable.scoobydoo)
    ;
    private String categoryName;
    private int drawableIcon;

    CARTOON_CATEGORY(String categoryName, int drawableIcon) {
        this.categoryName = categoryName;
        this.drawableIcon = drawableIcon;
    }

    public int getDrawableIcon() {
        return drawableIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    private static final Map<String, CARTOON_CATEGORY> map = new HashMap<>(values().length, 1);
    static {
        for (CARTOON_CATEGORY c : values()) map.put(c.categoryName, c);
    }

    public static CARTOON_CATEGORY getCartoon_Category(String name) {
        CARTOON_CATEGORY result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("Invalid category name: " + name);
        }
        return result;
    }

    public static Drawable getCartoonDrawable(Context context,String categoryName){
        CARTOON_CATEGORY cartoon_category = getCartoon_Category(categoryName);
        return  context.getResources().getDrawable(cartoon_category.getDrawableIcon());
    }
    public static int getDrawableIcon(Context context,String categoryName){
        CARTOON_CATEGORY cartoon_category = getCartoon_Category(categoryName);
        return  cartoon_category.getDrawableIcon();

    }
}
