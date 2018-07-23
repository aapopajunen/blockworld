package aapopajunen.entity;

import aapopajunen.engine.graphical.Model;

public enum Direction {
    X_POS, X_NEG, Y_POS, Y_NEG, Z_POS, Z_NEG;

    public static byte directionToByte(Direction direction) {
        switch (direction) {
            case X_POS: return 0b00100000;
            case X_NEG: return 0b00010000;
            case Y_POS: return 0b00001000;
            case Y_NEG: return 0b00000100;
            case Z_POS: return 0b00000010;
            case Z_NEG: return 0b00000001;
            default: return 0;
        }
    }

    public static Model directionToFace(Direction direction) {
        Model face = new Model();
        float[] vertices = null;
        switch (direction) {
            case X_POS: {
                vertices = new float[] {
                        1,0,0,
                        1,0,1,
                        1,1,0,
                        1,1,1
                };
                break;
            }
            case X_NEG: {
                vertices = new float[] {
                        0,0,0,
                        0,0,1,
                        0,1,0,
                        0,1,1
                };
                break;
            }
            case Y_POS: {
                vertices = new float[] {
                        0,1,0,
                        0,1,1,
                        1,1,0,
                        1,1,1
                };
                break;
            }
            case Y_NEG: {
                vertices = new float[] {
                        0,0,0,
                        0,0,1,
                        1,0,0,
                        1,0,1
                };
                break;
            }
            case Z_POS: {
                vertices = new float[] {
                        0,0,1,
                        0,1,1,
                        1,0,1,
                        1,1,1
                };
                break;
            }
            case Z_NEG: {
                vertices = new float[] {
                        0,0,0,
                        0,1,0,
                        1,0,0,
                        1,1,0
                };
                break;
            }
            default: return null;
        }
        face.setVertices(vertices);
        return face;
    }


}
