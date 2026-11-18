package mods.Hileb.optirefine.optifine.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

public final class NothiriumQuadOrder {
    private static final ThreadLocal<NothiriumQuadOrder> CAPTURE = new ThreadLocal<>();

    private Object vertexData;
    private int[] order;

    private NothiriumQuadOrder() {}

    public static int[] sort(Object vertexData, int vertexCount, int vertexSize, int primitiveSize,
                             float dx, float dy, float dz, Operation<Void> original) {
        NothiriumQuadOrder capture = CAPTURE.get();
        if (capture == null) {
            capture = new NothiriumQuadOrder();
            CAPTURE.set(capture);
        }
        Object previousData = capture.vertexData;
        int[] previousOrder = capture.order;
        capture.vertexData = vertexData;
        capture.order = null;
        try {
            original.call(vertexData, vertexCount, vertexSize, primitiveSize, dx, dy, dz);
            return capture.order;
        } finally {
            capture.vertexData = previousData;
            capture.order = previousOrder;
        }
    }

    public static void capture(Object vertexData, int[] order) {
        NothiriumQuadOrder capture = CAPTURE.get();
        if (capture != null && capture.vertexData == vertexData) {
            capture.order = order;
        }
    }
}
