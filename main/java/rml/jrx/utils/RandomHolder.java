package rml.jrx.utils;

import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.util.Random;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/4/3 18:27
 **/
@EarlyClass
@PublicAPI
public class RandomHolder {
    public static final Random RANDOM = new Random();
}
