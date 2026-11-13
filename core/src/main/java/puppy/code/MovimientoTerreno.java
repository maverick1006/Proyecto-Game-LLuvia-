package puppy.code;

import com.badlogic.gdx.math.MathUtils;

public enum MovimientoTerreno {
    NORMAL,
    HIELO {
        @Override float calcular(float pedido, float anterior, float delta) {
            return MathUtils.lerp(anterior, pedido, Math.min(1, 5 * delta));
        }
    },
    ATACAMA {
        @Override float calcular(float pedido, float anterior, float delta) {
            return pedido + 45 * delta;
        }
    },
    ESPACIO {
        @Override float calcular(float pedido, float anterior, float delta) {
            return pedido * 1.2f;
        }
    };

    float calcular(float pedido, float anterior, float delta) { return pedido; }
}
