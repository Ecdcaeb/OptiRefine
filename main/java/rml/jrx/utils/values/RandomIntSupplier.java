package rml.jrx.utils.values;

import rml.jrx.announces.BeDiscovered;
import rml.jrx.announces.PublicAPI;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import rml.deserializer.AbstractDeserializer;
import rml.loader.deserialize.Deserializer;
import rml.deserializer.Record;

import java.util.Random;

/**
 * @Project CustomVillage
 * @Author Hileb
 * @Date 2023/8/20 9:25
 **/
@PublicAPI
@BeDiscovered(BeDiscovered.PRE_INIT)
public interface RandomIntSupplier {
    AbstractDeserializer<RandomIntSupplier> DESERIALIZER = Deserializer.MANAGER.addDefaultEntry(new AbstractDeserializer<>(new ResourceLocation("rml", "default_constant"), RandomIntSupplier.class,
            jsonElement -> new RangeConstant(Deserializer.decode(Integer.class, jsonElement))));
    int get(Random random);

    /**
     * @Project CustomVillage
     * @Author Hileb
     * @Date 2023/8/20 9:25
     **/
    @BeDiscovered(BeDiscovered.PRE_INIT)
    @Record
    class RangePrice implements RandomIntSupplier {
        public static final AbstractDeserializer<RandomIntSupplier> DESERIALIZER = Deserializer.named(RandomIntSupplier.class, new ResourceLocation("minecraft","price"))
                .record(RangePrice.class).markDefault().build();
        EntityVillager.PriceInfo info;

        @Record({"min", "max"})
        public RangePrice(int min, int max){
            info = new EntityVillager.PriceInfo(min, max);
        }

        @Override
        public int get(Random random) {
            return info.getPrice(random);
        }
    }

    /**
     * @Project CustomVillage
     * @Author Hileb
     * @Date 2023/8/20 9:28
     **/
    @BeDiscovered(BeDiscovered.PRE_INIT)
    @Record
    class RangeConstant implements RandomIntSupplier {

        public static final AbstractDeserializer<RandomIntSupplier> DESERIALIZER = Deserializer.named(RandomIntSupplier.class, new ResourceLocation("cvh","constant"))
                .record(RangeConstant.class).markDefault().build();
        public static final RangeConstant ONE = new RangeConstant(1);
        final int a;

        @Record({"constant"})
        public RangeConstant(int aIn){
            a=aIn;
        }

        @Override
        public int get(Random random) {
            return a;
        }
    }

    /**
     * @Project CustomVillage
     * @Author Hileb
     * @Date 2023/8/20 10:16
     **/
    @BeDiscovered(BeDiscovered.PRE_INIT)
    @Record
    class RangePoisson implements RandomIntSupplier {

        public static final AbstractDeserializer<RandomIntSupplier> DESERIALIZER = Deserializer.named(RandomIntSupplier.class, new ResourceLocation("cvh","poisson_distribution"))
                .record(RangePoisson.class).markDefault().build();
        public final int min;
        public final int max;
        public final int lambda;

        @Record({"min","max", "lambda"})
        public RangePoisson(int minIn, int maxIn, int lambdaIn){
            max=maxIn;
            min=minIn;
            lambda=lambdaIn;
        }

        @Override
        public int get(Random random) {
            return getRandomlyInRange(random,lambda,min,max);
        }

        public static double getChance(int lambda,int r){
            double top1=Math.pow(Math.E,-lambda);
            double top2=Math.pow(lambda,r);
            double bottom= factorial(r);
            return ((top1*top2)/bottom);
        }
        public static int getRandomlyInRange(Random random,int lambda, int min, int max){
            // [min,max)
            final int length=max-min;
            double[] chances=new double[length];
            double all=0;
            for(int i=0;i<length;i++){
                int c=min+i;
                chances[i]=getChance(lambda,c);
                all+=chances[i];
            }
            double t=random.nextDouble();
            t=t*all;
            for(int i=0;i<length;i++){
                double chance=chances[i];
                if (t>chance){
                    t-=chance;
                    continue;
                }else {
                    return i+min;
                }
            }
            return (min+max)/2;
        }
        public static int factorial(int a){
            if (a<0){
                throw new IllegalArgumentException("factorial error at:"+a);
            }
            else if (a==0 || a==1){
                return 1;
            }else {
                int sum=1;
                for(int i=1;i<=a;i++){
                    sum*=i;
                }
                return sum;
            }
        }
    }
}
