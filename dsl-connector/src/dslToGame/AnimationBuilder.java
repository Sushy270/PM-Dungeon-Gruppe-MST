package dslToGame;

import graphic.Animation;
import graphic.textures.TextureHandler;
import semanticAnalysis.types.DSLTypeAdapter;

import java.util.List;

public class AnimationBuilder {
    public static int frameTime = 5;
//    private static boolean toggle = false;

    @DSLTypeAdapter(t = Animation.class)
    public static Animation buildAnimation(String path) {
//        if(!toggle) {
//            List<String> list = TextureHandler.getInstance().getTexturePaths(path);
//            for(String s: list)
//                System.out.println(s + "\n");
//            toggle = true;
//        }
        return new Animation(TextureHandler.getInstance().getTexturePaths(path), frameTime);
    }


//    public static String buildPath(Animation animation) {
//
//    }
}
