package com.client;

import net.runelite.api.Perspective;
import net.runelite.api.model.Jarvis;
import net.runelite.api.model.Triangle;
import net.runelite.api.model.Vertex;
import net.runelite.rs.api.RSFrames;
import net.runelite.rs.api.RSModel;

import java.awt.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.List;

public class Model extends Renderable implements RSModel {
    private static final int method4626(int var0, int var1, int var2, int var3) {
        return var0 * var2 + var3 * var1 >> 16;
    }

    private static final int method4663(int var0, int var1, int var2, int var3) {
        return var2 * var1 - var3 * var0 >> 16;
    }

    private void calculateBoundingBox(int var1) {
        if (this.xMidOffset == -1) {
            int var2 = 0;
            int var3 = 0;
            int var4 = 0;
            int var5 = 0;
            int var6 = 0;
            int var7 = 0;
            int var8 = COSINE[var1];
            int var9 = SINE[var1];

            for (int var10 = 0; var10 < this.getVerticesCount(); ++var10) {
                int var11 = method4626(this.getVerticesX()[var10], this.getVerticesZ()[var10], var8, var9);
                int var12 = this.getVerticesY()[var10];
                int var13 = method4663(this.getVerticesX()[var10], this.getVerticesZ()[var10], var8, var9);
                if (var11 < var2) {
                    var2 = var11;
                }

                if (var11 > var5) {
                    var5 = var11;
                }

                if (var12 < var3) {
                    var3 = var12;
                }

                if (var12 > var6) {
                    var6 = var12;
                }

                if (var13 < var4) {
                    var4 = var13;
                }

                if (var13 > var7) {
                    var7 = var13;
                }
            }

            this.xMid = (var5 + var2) / 2;
            this.yMid = (var6 + var3) / 2;
            this.zMid = (var7 + var4) / 2;
            this.xMidOffset = (var5 - var2 + 1) / 2;
            this.yMidOffset = (var6 - var3 + 1) / 2;
            this.zMidOffset = (var7 - var4 + 1) / 2;
            if (this.xMidOffset < 32) {
                this.xMidOffset = 32;
            }

            if (this.zMidOffset < 32) {
                this.zMidOffset = 32;
            }

            if (this.fits_on_single_square) {
                this.xMidOffset += 8;
                this.zMidOffset += 8;
            }
        }
    }

    private int xMid;
    private int yMid;
    private int zMid;
    private int xMidOffset;
    private int yMidOffset;
    private int zMidOffset;
    public static void nullLoader() {
        aClass21Array1661 = null;
        hasAnEdgeToRestrict = null;
        outOfReach = null;
        projected_verticesY = null;
        projected_verticesZ = null;
        anIntArray1668 = null;
        camera_verticesY = null;
        viewportTextureZ = null;
        depthListIndices = null;
        faceLists = null;
        anIntArray1673 = null;
        anIntArrayArray1674 = null;
        anIntArray1675 = null;
        anIntArray1676 = null;
        anIntArray1677 = null;
        SINE = null;
        COSINE = null;
        hsl2rgb = null;
        lightDecay = null;
    }

    public void convertTexturesTo317(short[] textureIds, int[] texa, int[] texb, int[] texc, byte[] texture_coordinates) {
        int set = 0;
        int set2 = 0;
        int max = 50;
        if (textureIds != null) {
            texturesX = new short[trianglesCount];
            texturesY = new short[trianglesCount];
            texturesZ = new short[trianglesCount];


            for (int i = 0; i < trianglesCount; i++) {
                if (textureIds[i] == -1 && types[i] == 2) {
                    colors[i] = (short) 65535;
                    types[i] = 0;
                }
                if (textureIds[i] >= max || textureIds[i] < 0 || textureIds[i] == 39) {
                    types[i] = 0;
                    continue;
                }
                types[i] = 2 + set2;
                set2 += 4;
                int a = trianglesX[i];
                int b = trianglesY[i];
                int c = trianglesZ[i];
                colors[i] = textureIds[i];


                int texture_type = -1;
                if (texture_coordinates != null) {
                    texture_type = texture_coordinates[i] & 0xff;
                    if (texture_type != 0xff)
                        if (texa[texture_type] >= camera_verticesY.length || texb[texture_type] >= anIntArray1668.length
                                || texc[texture_type] >= viewportTextureZ.length)
                            texture_type = -1;
                }
                if (texture_type == 0xff)
                    texture_type = -1;


                texturesX[set] = (short) (texture_type == -1 ? a : texa[texture_type]);
                texturesY[set] = (short) (texture_type == -1 ? b : texb[texture_type]);
                texturesZ[set++] = (short) (texture_type == -1 ? c : texc[texture_type]);


            }
            texturesCount = set;
        }
    }

    public void convertTexturesTo317(short[] textureIds, int[] texa, int[] texb, int[] texc, boolean osrs,
                                     byte[] texture_coordinates) {
        int set = 0;
        int set2 = 0;
        int max = Rasterizer.textureAmount;
        if (textureIds != null) {
            texturesX = new short[trianglesCount];
            texturesY = new short[trianglesCount];
            texturesZ = new short[trianglesCount];

            for (int i = 0; i < trianglesCount; i++) {
                if (textureIds[i] == -1 && types[i] == 2) {
                    types[i] = 0;
                }
                if (textureIds[i] >= max || textureIds[i] < 0 || textureIds[i] == 39) {
                    types[i] = 0;
                    continue;
                }
                types[i] = 2 + set2;
                set2 += 4;
                int a = trianglesX[i];
                int b = trianglesY[i];
                int c = trianglesZ[i];
                colors[i] = textureIds[i];

                int texture_type = -1;
                if (texture_coordinates != null) {
                    texture_type = texture_coordinates[i] & 0xff;
                    if (texture_type != 0xff)
                        if (texa[texture_type] >= camera_verticesY.length || texb[texture_type] >= anIntArray1668.length
                                || texc[texture_type] >= viewportTextureZ.length)
                            texture_type = -1;
                }
                if (texture_type == 0xff)
                    texture_type = -1;

                texturesX[set] = (short) (texture_type == -1 ? a : texa[texture_type]);
                texturesY[set] = (short) (texture_type == -1 ? b : texb[texture_type]);
                texturesZ[set++] = (short) (texture_type == -1 ? c : texc[texture_type]);

            }
            texturesCount = set;
        }
    }

    public void method476(int i, int j) {
        for (int k = 0; k < trianglesCount; k++)
            if (colors[k] == i)
                colors[k] = (short) j;

    }

    public void replaceColor(int i, int j, int tex) {
        if (tex == -1) {
            for (int k = 0; k < trianglesCount; k++)
                if (colors[k] == i)
                    colors[k] = (short) j;
        } else {
            texturesCount = trianglesCount;
            int set2 = 0;
            if (types == null)
                types = new int[trianglesCount];
            if (colors == null)
                colors = new short[trianglesCount];
            texturesX = new short[trianglesCount];
            texturesY = new short[trianglesCount];
            texturesZ = new short[trianglesCount];
            for (int i3 = 0; i3 < trianglesCount; i3++) {
                if (colors[i3] != 0) {
                    colors[i3] = (short) tex;
                    types[i3] = 3 + set2;
                    set2 += 4;
                    texturesX[i3] = (short) trianglesX[i3];
                    texturesY[i3] = (short) trianglesY[i3];
                    texturesZ[i3] = (short) trianglesZ[i3];
                }
            }
        }
    }


 /*   public void read525Model(byte abyte0[], int modelID) {
        Buffer nc1 = new Buffer(abyte0);
        Buffer nc2 = new Buffer(abyte0);
        Buffer nc3 = new Buffer(abyte0);
        Buffer nc4 = new Buffer(abyte0);
        Buffer nc5 = new Buffer(abyte0);
        Buffer nc6 = new Buffer(abyte0);
        Buffer nc7 = new Buffer(abyte0);
        nc1.currentOffset = abyte0.length - 23;
        int numVertices = nc1.readUShort();
        int numTriangles = nc1.readUShort();
        int numTexTriangles = nc1.readUnsignedByte();
        ModelHeader ModelDef_1 = aClass21Array1661[modelID] = new ModelHeader();
        ModelDef_1.aByteArray368 = abyte0;
        ModelDef_1.anInt369 = numVertices;
        ModelDef_1.anInt370 = numTriangles;
        ModelDef_1.anInt371 = numTexTriangles;
        int l1 = nc1.readUnsignedByte();
        boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
        int i2 = nc1.readUnsignedByte();
        int j2 = nc1.readUnsignedByte();
        int k2 = nc1.readUnsignedByte();
        int l2 = nc1.readUnsignedByte();
        int i3 = nc1.readUnsignedByte();
        int j3 = nc1.readUShort();
        int k3 = nc1.readUShort();
        int l3 = nc1.readUShort();
        int i4 = nc1.readUShort();
        int j4 = nc1.readUShort();
        int k4 = 0;
        int l4 = 0;
        int i5 = 0;
        byte[] x = null;
        byte[] O = null;
        byte[] J = null;
        byte[] F = null;
        byte[] cb = null;
        byte[] gb = null;
        byte[] lb = null;
        int[] kb = null;
        int[] y = null;
        int[] N = null;
        short[] D = null;
        int[] triangleColours2 = new int[numTriangles];
        if (numTexTriangles > 0) {
            O = new byte[numTexTriangles];
            nc1.currentOffset = 0;
            for (int j5 = 0; j5 < numTexTriangles; j5++) {
                byte byte0 = O[j5] = nc1.readSignedByte();
                if (byte0 == 0) {
                    k4++;
                }
                if (byte0 >= 1 && byte0 <= 3) {
                    l4++;
                }
                if (byte0 == 2) {
                    i5++;
                }
            }
        }
        int k5 = numTexTriangles;
        int l5 = k5;
        k5 += numVertices;
        int i6 = k5;
        if (l1 == 1) {
            k5 += numTriangles;
        }
        int j6 = k5;
        k5 += numTriangles;
        int k6 = k5;
        if (i2 == 255) {
            k5 += numTriangles;
        }
        int l6 = k5;
        if (k2 == 1) {
            k5 += numTriangles;
        }
        int i7 = k5;
        if (i3 == 1) {
            k5 += numVertices;
        }
        int j7 = k5;
        if (j2 == 1) {
            k5 += numTriangles;
        }
        int k7 = k5;
        k5 += i4;
        int l7 = k5;
        if (l2 == 1) {
            k5 += numTriangles * 2;
        }
        int i8 = k5;
        k5 += j4;
        int j8 = k5;
        k5 += numTriangles * 2;
        int k8 = k5;
        k5 += j3;
        int l8 = k5;
        k5 += k3;
        int i9 = k5;
        k5 += l3;
        int j9 = k5;
        k5 += k4 * 6;
        int k9 = k5;
        k5 += l4 * 6;
        int l9 = k5;
        k5 += l4 * 6;
        int i10 = k5;
        k5 += l4;
        int j10 = k5;
        k5 += l4;
        int k10 = k5;
        k5 += l4 + i5 * 2;
        int[] vertexX = new int[numVertices];
        int[] vertexY = new int[numVertices];
        int[] vertexZ = new int[numVertices];
        int[] facePoint1 = new int[numTriangles];
        int[] facePoint2 = new int[numTriangles];
        int[] facePoint3 = new int[numTriangles];
        vertexData = new int[numVertices];
        types = new int[numTriangles];
        face_render_priorities = new int[numTriangles];
        alphas = new int[numTriangles];
        triangleData = new int[numTriangles];
        if (i3 == 1) {
            vertexData = new int[numVertices];
        }
        if (bool) {
            types = new int[numTriangles];
        }
        if (i2 == 255) {
            face_render_priorities = new int[numTriangles];
        } else {
        }
        if (j2 == 1) {
            alphas = new int[numTriangles];
        }
        if (k2 == 1) {
            triangleData = new int[numTriangles];
        }
        if (l2 == 1) {
            D = new short[numTriangles];
        }
        if (l2 == 1 && numTexTriangles > 0) {
            x = new byte[numTriangles];
        }
        triangleColours2 = new int[numTriangles];
        int[] texTrianglesPoint1 = null;
        int[] texTrianglesPoint2 = null;
        int[] texTrianglesPoint3 = null;
        if (numTexTriangles > 0) {
            texTrianglesPoint1 = new int[numTexTriangles];
            texTrianglesPoint2 = new int[numTexTriangles];
            texTrianglesPoint3 = new int[numTexTriangles];
            if (l4 > 0) {
                kb = new int[l4];
                N = new int[l4];
                y = new int[l4];
                gb = new byte[l4];
                lb = new byte[l4];
                F = new byte[l4];
            }
            if (i5 > 0) {
                cb = new byte[i5];
                J = new byte[i5];
            }
        }
        nc1.currentOffset = l5;
        nc2.currentOffset = k8;
        nc3.currentOffset = l8;
        nc4.currentOffset = i9;
        nc5.currentOffset = i7;
        int l10 = 0;
        int i11 = 0;
        int j11 = 0;
        for (int k11 = 0; k11 < numVertices; k11++) {
            int l11 = nc1.readUnsignedByte();
            int j12 = 0;
            if ((l11 & 1) != 0) {
                j12 = nc2.method421();
            }
            int l12 = 0;
            if ((l11 & 2) != 0) {
                l12 = nc3.method421();
            }
            int j13 = 0;
            if ((l11 & 4) != 0) {
                j13 = nc4.method421();
            }
            vertexX[k11] = l10 + j12;
            vertexY[k11] = i11 + l12;
            vertexZ[k11] = j11 + j13;
            l10 = vertexX[k11];
            i11 = vertexY[k11];
            j11 = vertexZ[k11];
            if (vertexData != null) {

                vertexData[k11] = nc5.readUnsignedByte();
            }
        }
        nc1.currentOffset = j8;
        nc2.currentOffset = i6;
        nc3.currentOffset = k6;
        nc4.currentOffset = j7;
        nc5.currentOffset = l6;
        nc6.currentOffset = l7;
        nc7.currentOffset = i8;
        for (int i12 = 0; i12 < numTriangles; i12++) {
            triangleColours2[i12] = nc1.readUShort();
            if (l1 == 1) {
                types[i12] = nc2.readSignedByte();
                if (types[i12] == 2) {
                    triangleColours2[i12] = 65535;
                }
                types[i12] = 0;
            }
            if (i2 == 255) {
                face_render_priorities[i12] = nc3.readSignedByte();
            }
            if (j2 == 1) {
                alphas[i12] = nc4.readSignedByte();
                if (alphas[i12] < 0) {
                    alphas[i12] = 256 + alphas[i12];
                }
            }
            if (k2 == 1) {
                triangleData[i12] = nc5.readUnsignedByte();
            }
            if (l2 == 1) {
                D[i12] = (short) (nc6.readUShort() - 1);
            }
            if (x != null) {
                if (D[i12] != -1) {
                    x[i12] = (byte) (nc7.readUnsignedByte() - 1);
                } else {
                    x[i12] = -1;
                }
            }
        }*/
        // /fix's triangle issue, but fucked up - no need, loading all 474-
        // models
        /*
         * try { for(int i12 = 0; i12 < numberOfTriangleFaces; i12++) {
		 * triangleColours2[i12] = nc1.readUnsignedWord(); if(l1 == 1){
		 * face_render_type[i12] = nc2.readSignedByte(); } if(i2 == 255){
		 * face_render_priorities[i12] = nc3.readSignedByte(); } if(j2 == 1){
		 * anIntArray1639[i12] = nc4.readSignedByte(); if(anIntArray1639[i12] <
		 * 0) anIntArray1639[i12] = (256+anIntArray1639[i12]); } if(k2 == 1)
		 * anIntArray1656[i12] = nc5.readUnsignedByte(); if(l2 == 1) D[i12] =
		 * (short)(nc6.readUnsignedWord() - 1); if(x != null) if(D[i12] != -1)
		 * x[i12] = (byte)(nc7.readUnsignedByte() -1); else x[i12] = -1; } }
		 * catch (Exception ex) { }
		 */
       /* nc1.currentOffset = k7;
        nc2.currentOffset = j6;
        int k12 = 0;
        int i13 = 0;
        int k13 = 0;
        int l13 = 0;
        for (int i14 = 0; i14 < numTriangles; i14++) {
            int j14 = nc2.readUnsignedByte();
            if (j14 == 1) {
                k12 = nc1.method421() + l13;
                l13 = k12;
                i13 = nc1.method421() + l13;
                l13 = i13;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 2) {
                i13 = k13;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 3) {
                k12 = k13;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 4) {
                int l14 = k12;
                k12 = i13;
                i13 = l14;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
        }
        nc1.currentOffset = j9;
        nc2.currentOffset = k9;
        nc3.currentOffset = l9;
        nc4.currentOffset = i10;
        nc5.currentOffset = j10;
        nc6.currentOffset = k10;
        for (int k14 = 0; k14 < numTexTriangles; k14++) {
            int i15 = O[k14] & 0xff;
            if (i15 == 0) {
                texTrianglesPoint1[k14] = nc1.readUShort();
                texTrianglesPoint2[k14] = nc1.readUShort();
                texTrianglesPoint3[k14] = nc1.readUShort();
            }
            if (i15 == 1) {
                texTrianglesPoint1[k14] = nc2.readUShort();
                texTrianglesPoint2[k14] = nc2.readUShort();
                texTrianglesPoint3[k14] = nc2.readUShort();
                kb[k14] = nc3.readUShort();
                N[k14] = nc3.readUShort();
                y[k14] = nc3.readUShort();
                gb[k14] = nc4.readSignedByte();
                lb[k14] = nc5.readSignedByte();
                F[k14] = nc6.readSignedByte();
            }
            if (i15 == 2) {
                texTrianglesPoint1[k14] = nc2.readUShort();
                texTrianglesPoint2[k14] = nc2.readUShort();
                texTrianglesPoint3[k14] = nc2.readUShort();
                kb[k14] = nc3.readUShort();
                N[k14] = nc3.readUShort();
                y[k14] = nc3.readUShort();
                gb[k14] = nc4.readSignedByte();
                lb[k14] = nc5.readSignedByte();
                F[k14] = nc6.readSignedByte();
                cb[k14] = nc6.readSignedByte();
                J[k14] = nc6.readSignedByte();
            }
            if (i15 == 3) {
                texTrianglesPoint1[k14] = nc2.readUShort();
                texTrianglesPoint2[k14] = nc2.readUShort();
                texTrianglesPoint3[k14] = nc2.readUShort();
                kb[k14] = nc3.readUShort();
                N[k14] = nc3.readUShort();
                y[k14] = nc3.readUShort();
                gb[k14] = nc4.readSignedByte();
                lb[k14] = nc5.readSignedByte();
                F[k14] = nc6.readSignedByte();
            }
        }
        if (i2 != 255) {
            for (int i12 = 0; i12 < numTriangles; i12++) {
                face_render_priorities[i12] = i2;
            }
        }
        colors = triangleColours2;
        this.verticesCount = numVertices;
        this.trianglesCount = numTriangles;
        this.verticesX = vertexX;
        this.verticesY = vertexY;
        this.verticesZ = vertexZ;
        trianglesX = facePoint1;
        trianglesY = facePoint2;
        trianglesZ = facePoint3;
        convertTexturesTo317(D, texTrianglesPoint1, texTrianglesPoint2, texTrianglesPoint3, false, x);
    }*/

    public static final byte[] read(String location) {
        try {
            File read = new File(location);
            int size = (int) read.length();
            byte data[] = new byte[size];
            try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(location)))) {
                input.readFully(data, 0, size);
                input.close();
                return data;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Model(int model) {
        if (model == 0) {
            return;
        }

        if (model == 39284) { // broken models
            return;
        }

        try {


            byte[] data = aClass21Array1661[model].aByteArray368;

            if (data[data.length - 1] == -3 && data[data.length - 2] == -1) {
                ModelLoader.decodeType3(this, data);
            } else if (data[data.length - 1] == -2 && data[data.length - 2] == -1) {
                ModelLoader.decodeType2(this, data);
            } else if (data[data.length - 1] == -1 && data[data.length - 2] == -1) {
                ModelLoader.decodeType1(this, data);
            } else {
                ModelLoader.decodeOldFormat(this, data);
            }
            if (newmodel[model]) {
                scale2(4);// 2 is too big -- 3 is almost right
                if (face_render_priorities != null) {
                    for (int j = 0; j < face_render_priorities.length; j++) {
                        face_render_priorities[j] = 10;
                        this.face_priority = 10;
                    }
                }
            }

            /**
             * Manual Invisible Fix
             **/
            int[] invisibleFix = { 65130, 65131, 65133, 65135, 65131};
            for (int i1 = 0; i1 < invisibleFix.length; i1++) {
                if (id == invisibleFix[i1]) {
                    scale2(3);
                    if (face_render_priorities != null) {
                        for (int j = 0; j < face_render_priorities.length; j++) {
                            face_render_priorities[j] = 10;
                           // this.face_priority = 10;
                        }
                    }
                }
            }
            /**
             * Black Fix
             **/
            for (int i2 = 0; i2 < trianglesCount; i2++) {
                //scale2(4);
                if (colors != null) {
                    if (colors[i2] == 0) {
                        colors[i2] = 1;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error decoding model: " + model);
            e.printStackTrace();
        }
    }

    public void scale2(int i) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] = verticesX[i1] / i;
            verticesY[i1] = verticesY[i1] / i;
            verticesZ[i1] = verticesZ[i1] / i;
        }
    }

    public void read622Model(byte abyte0[], int modelID) {
        Buffer nc1 = new Buffer(abyte0);
        Buffer nc2 = new Buffer(abyte0);
        Buffer nc3 = new Buffer(abyte0);
        Buffer nc4 = new Buffer(abyte0);
        Buffer nc5 = new Buffer(abyte0);
        Buffer nc6 = new Buffer(abyte0);
        Buffer nc7 = new Buffer(abyte0);
        nc1.currentOffset = abyte0.length - 23;
        int numVertices = nc1.readUShort();
        int numTriangles = nc1.readUShort();
        int numTexTriangles = nc1.readUnsignedByte();
        ModelHeader ModelDef_1 = aClass21Array1661[modelID] = new ModelHeader();
        ModelDef_1.aByteArray368 = abyte0;
        ModelDef_1.anInt369 = numVertices;
        ModelDef_1.anInt370 = numTriangles;
        ModelDef_1.anInt371 = numTexTriangles;
        int l1 = nc1.readUnsignedByte();
        boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
        boolean bool_26_ = (0x8 & l1) == 8;
//        if (!bool_26_) {
//            read525Model(abyte0, modelID);
//            return;
//        }
        int newformat = 0;
        if (bool_26_) {
            nc1.currentOffset -= 7;
            newformat = nc1.readUnsignedByte();
            nc1.currentOffset += 6;
        }
        if (newformat == 15) {
            newmodel[modelID] = true;
        }
        int i2 = nc1.readUnsignedByte();
        int j2 = nc1.readUnsignedByte();
        int k2 = nc1.readUnsignedByte();
        int l2 = nc1.readUnsignedByte();
        int i3 = nc1.readUnsignedByte();
        int j3 = nc1.readUShort();
        int k3 = nc1.readUShort();
        int l3 = nc1.readUShort();
        int i4 = nc1.readUShort();
        int j4 = nc1.readUShort();
        int k4 = 0;
        int l4 = 0;
        int i5 = 0;
        byte[] x = null;
        byte[] O = null;
        byte[] J = null;
        byte[] F = null;
        byte[] cb = null;
        byte[] gb = null;
        byte[] lb = null;
        int[] kb = null;
        int[] y = null;
        int[] N = null;
        short[] D = null;
        int[] triangleColours2 = new int[numTriangles];
        if (numTexTriangles > 0) {
            O = new byte[numTexTriangles];
            nc1.currentOffset = 0;
            for (int j5 = 0; j5 < numTexTriangles; j5++) {
                byte byte0 = O[j5] = nc1.readSignedByte();
                if (byte0 == 0) {
                    k4++;
                }
                if (byte0 >= 1 && byte0 <= 3) {
                    l4++;
                }
                if (byte0 == 2) {
                    i5++;
                }
            }
        }
        int k5 = numTexTriangles;
        int l5 = k5;
        k5 += numVertices;
        int i6 = k5;
        if (bool) {
            k5 += numTriangles;
        }
        if (l1 == 1) {
            k5 += numTriangles;
        }
        int j6 = k5;
        k5 += numTriangles;
        int k6 = k5;
        if (i2 == 255) {
            k5 += numTriangles;
        }
        int l6 = k5;
        if (k2 == 1) {
            k5 += numTriangles;
        }
        int i7 = k5;
        if (i3 == 1) {
            k5 += numVertices;
        }
        int j7 = k5;
        if (j2 == 1) {
            k5 += numTriangles;
        }
        int k7 = k5;
        k5 += i4;
        int l7 = k5;
        if (l2 == 1) {
            k5 += numTriangles * 2;
        }
        int i8 = k5;
        k5 += j4;
        int j8 = k5;
        k5 += numTriangles * 2;
        int k8 = k5;
        k5 += j3;
        int l8 = k5;
        k5 += k3;
        int i9 = k5;
        k5 += l3;
        int j9 = k5;
        k5 += k4 * 6;
        int k9 = k5;
        k5 += l4 * 6;
        int i_59_ = 6;
        if (newformat != 14) {
            if (newformat >= 15) {
                i_59_ = 9;
            }
        } else {
            i_59_ = 7;
        }
        int l9 = k5;
        k5 += i_59_ * l4;
        int i10 = k5;
        k5 += l4;
        int j10 = k5;
        k5 += l4;
        int k10 = k5;
        k5 += l4 + i5 * 2;
        int[] vertexX = new int[numVertices];
        int[] vertexY = new int[numVertices];
        int[] vertexZ = new int[numVertices];
        int[] facePoint1 = new int[numTriangles];
        int[] facePoint2 = new int[numTriangles];
        int[] facePoint3 = new int[numTriangles];
        vertexData = new int[numVertices];
        types = new int[numTriangles];
        face_render_priorities = new byte [numTriangles];
        alphas = new int[numTriangles];
        triangleData = new int[numTriangles];
        if (i3 == 1) {
            vertexData = new int[numVertices];
        }
        if (bool) {
            types = new int[numTriangles];
        }
        if (i2 == 255) {
            face_render_priorities = new byte[numTriangles];
        } else {
        }
        if (j2 == 1) {
            alphas = new int[numTriangles];
        }
        if (k2 == 1) {
            triangleData = new int[numTriangles];
        }
        if (l2 == 1) {
            D = new short[numTriangles];
        }
        if (l2 == 1 && numTexTriangles > 0) {
            x = new byte[numTriangles];
        }
        triangleColours2 = new int[numTriangles];
        int[] texTrianglesPoint1 = null;
        int[] texTrianglesPoint2 = null;
        int[] texTrianglesPoint3 = null;
        if (numTexTriangles > 0) {
            texTrianglesPoint1 = new int[numTexTriangles];
            texTrianglesPoint2 = new int[numTexTriangles];
            texTrianglesPoint3 = new int[numTexTriangles];
            if (l4 > 0) {
                kb = new int[l4];
                N = new int[l4];
                y = new int[l4];
                gb = new byte[l4];
                lb = new byte[l4];
                F = new byte[l4];
            }
            if (i5 > 0) {
                cb = new byte[i5];
                J = new byte[i5];
            }
        }
        nc1.currentOffset = l5;
        nc2.currentOffset = k8;
        nc3.currentOffset = l8;
        nc4.currentOffset = i9;
        nc5.currentOffset = i7;
        int l10 = 0;
        int i11 = 0;
        int j11 = 0;
        for (int k11 = 0; k11 < numVertices; k11++) {
            int l11 = nc1.readUnsignedByte();
            int j12 = 0;
            if ((l11 & 1) != 0) {
                j12 = nc2.method421();
            }
            int l12 = 0;
            if ((l11 & 2) != 0) {
                l12 = nc3.method421();
            }
            int j13 = 0;
            if ((l11 & 4) != 0) {
                j13 = nc4.method421();
            }
            vertexX[k11] = l10 + j12;
            vertexY[k11] = i11 + l12;
            vertexZ[k11] = j11 + j13;
            l10 = vertexX[k11];
            i11 = vertexY[k11];
            j11 = vertexZ[k11];
            if (vertexData != null) {
                vertexData[k11] = nc5.readUnsignedByte();
            }
        }
        nc1.currentOffset = j8;
        nc2.currentOffset = i6;
        nc3.currentOffset = k6;
        nc4.currentOffset = j7;
        nc5.currentOffset = l6;
        nc6.currentOffset = l7;
        nc7.currentOffset = i8;
        for (int i12 = 0; i12 < numTriangles; i12++) {
            triangleColours2[i12] = nc1.readUShort();
            if (l1 == 1) {
                types[i12] = nc2.readSignedByte();
                if (types[i12] == 2) {
                    triangleColours2[i12] = 65535;
                }
                types[i12] = 0;
            }
            if (i2 == 255) {
                face_render_priorities[i12] = nc3.readSignedByte();
            }
            if (j2 == 1) {
                alphas[i12] = nc4.readSignedByte();
                if (alphas[i12] < 0) {
                    alphas[i12] = 256 + alphas[i12];
                }
            }
            if (k2 == 1) {
                triangleData[i12] = nc5.readUnsignedByte();
            }
            if (l2 == 1) {
                D[i12] = (short) (nc6.readUShort() - 1);
            }
            if (x != null) {
                if (D[i12] != -1) {
                    x[i12] = (byte) (nc7.readUnsignedByte() - 1);
                } else {
                    x[i12] = -1;
                }
            }
        }
        nc1.currentOffset = k7;
        nc2.currentOffset = j6;
        int k12 = 0;
        int i13 = 0;
        int k13 = 0;
        int l13 = 0;
        for (int i14 = 0; i14 < numTriangles; i14++) {
            int j14 = nc2.readUnsignedByte();
            if (j14 == 1) {
                k12 = nc1.method421() + l13;
                l13 = k12;
                i13 = nc1.method421() + l13;
                l13 = i13;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 2) {
                i13 = k13;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 3) {
                k12 = k13;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 4) {
                int l14 = k12;
                k12 = i13;
                i13 = l14;
                k13 = nc1.method421() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
        }
        nc1.currentOffset = j9;
        nc2.currentOffset = k9;
        nc3.currentOffset = l9;
        nc4.currentOffset = i10;
        nc5.currentOffset = j10;
        nc6.currentOffset = k10;
        for (int k14 = 0; k14 < numTexTriangles; k14++) {
            int i15 = O[k14] & 0xff;
            if (i15 == 0) {
                texTrianglesPoint1[k14] = nc1.readUShort();
                texTrianglesPoint2[k14] = nc1.readUShort();
                texTrianglesPoint3[k14] = nc1.readUShort();
            }
            if (i15 == 1) {
                texTrianglesPoint1[k14] = nc2.readUShort();
                texTrianglesPoint2[k14] = nc2.readUShort();
                texTrianglesPoint3[k14] = nc2.readUShort();
                if (newformat < 15) {
                    kb[k14] = nc3.readUShort();
                    if (newformat >= 14) {
                        N[k14] = nc3.v(-1);
                    } else {
                        N[k14] = nc3.readUShort();
                    }
                    y[k14] = nc3.readUShort();
                } else {
                    kb[k14] = nc3.v(-1);
                    N[k14] = nc3.v(-1);
                    y[k14] = nc3.v(-1);
                }
                gb[k14] = nc4.readSignedByte();
                lb[k14] = nc5.readSignedByte();
                F[k14] = nc6.readSignedByte();
            }
            if (i15 == 2) {
                texTrianglesPoint1[k14] = nc2.readUShort();
                texTrianglesPoint2[k14] = nc2.readUShort();
                texTrianglesPoint3[k14] = nc2.readUShort();
                if (newformat >= 15) {
                    kb[k14] = nc3.v(-1);
                    N[k14] = nc3.v(-1);
                    y[k14] = nc3.v(-1);
                } else {
                    kb[k14] = nc3.readUShort();
                    if (newformat < 14) {
                        N[k14] = nc3.readUShort();
                    } else {
                        N[k14] = nc3.v(-1);
                    }
                    y[k14] = nc3.readUShort();
                }
                gb[k14] = nc4.readSignedByte();
                lb[k14] = nc5.readSignedByte();
                F[k14] = nc6.readSignedByte();
                cb[k14] = nc6.readSignedByte();
                J[k14] = nc6.readSignedByte();
            }
            if (i15 == 3) {
                texTrianglesPoint1[k14] = nc2.readUShort();
                texTrianglesPoint2[k14] = nc2.readUShort();
                texTrianglesPoint3[k14] = nc2.readUShort();
                if (newformat < 15) {
                    kb[k14] = nc3.readUShort();
                    if (newformat < 14) {
                        N[k14] = nc3.readUShort();
                    } else {
                        N[k14] = nc3.v(-1);
                    }
                    y[k14] = nc3.readUShort();
                } else {
                    kb[k14] = nc3.v(-1);
                    N[k14] = nc3.v(-1);
                    y[k14] = nc3.v(-1);
                }
                gb[k14] = nc4.readSignedByte();
                lb[k14] = nc5.readSignedByte();
                F[k14] = nc6.readSignedByte();
            }
        }
        if (i2 != 255) {
            for (int i12 = 0; i12 < numTriangles; i12++) {
                face_render_priorities[i12] = (byte) i2;
            }
        }
        colors = new short [trianglesCount];
        this.verticesCount = numVertices;
        this.trianglesCount = numTriangles;
        this.verticesX = vertexX;
        this.verticesY = vertexY;
        this.verticesZ = vertexZ;
        trianglesX = facePoint1;
        trianglesY = facePoint2;
        trianglesZ = facePoint3;
        convertTexturesTo317(D, texTrianglesPoint1, texTrianglesPoint2, texTrianglesPoint3, false, x);
    }

	/*private void readOldModel(int i) {
        int j = -870;
		aBoolean1618 = true;
		aBoolean1659 = false;
		anInt1620++;
		ModelHeader class21 = aClass21Array1661[i];
		numVertices = class21.anInt369;
		numberOfTriangleFaces = class21.anInt370;
		numberOfTexturesFaces = class21.anInt371;
		vertexX = new int[numVertices];
		vertexY = new int[numVertices];
		vertexZ = new int[numVertices];
		face_a = new int[numberOfTriangleFaces];
		face_b = new int[numberOfTriangleFaces];
		while (j >= 0) {
			aBoolean1618 = !aBoolean1618;
		}
		face_c = new int[numberOfTriangleFaces];
		textured_face_a = new int[numberOfTexturesFaces];
		textured_face_b = new int[numberOfTexturesFaces];
		textured_face_c = new int[numberOfTexturesFaces];
		if (class21.anInt376 >= 0) {
			anIntArray1655 = new int[numVertices];
		}
		if (class21.anInt380 >= 0) {
			face_render_type = new int[numberOfTriangleFaces];
		}
		if (class21.anInt381 >= 0) {
			face_render_priorities = new int[numberOfTriangleFaces];
		} else {
			anInt1641 = -class21.anInt381 - 1;
		}
		if (class21.anInt382 >= 0) {
			anIntArray1639 = new int[numberOfTriangleFaces];
		}
		if (class21.anInt383 >= 0) {
			anIntArray1656 = new int[numberOfTriangleFaces];
		}
		face_color = new int[numberOfTriangleFaces];
		Stream stream = new Stream(class21.aByteArray368);
		stream.currentOffset = class21.anInt372;
		Stream stream_1 = new Stream(class21.aByteArray368);
		stream_1.currentOffset = class21.anInt373;
		Stream stream_2 = new Stream(class21.aByteArray368);
		stream_2.currentOffset = class21.anInt374;
		Stream stream_3 = new Stream(class21.aByteArray368);
		stream_3.currentOffset = class21.anInt375;
		Stream stream_4 = new Stream(class21.aByteArray368);
		stream_4.currentOffset = class21.anInt376;
		int k = 0;
		int l = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < numVertices; j1++) {
			int k1 = stream.readUnsignedByte();
			int i2 = 0;
			if ((k1 & 1) != 0) {
				i2 = stream_1.method421();
			}
			int k2 = 0;
			if ((k1 & 2) != 0) {
				k2 = stream_2.method421();
			}
			int i3 = 0;
			if ((k1 & 4) != 0) {
				i3 = stream_3.method421();
			}
			vertexX[j1] = k + i2;
			vertexY[j1] = l + k2;
			vertexZ[j1] = i1 + i3;
			k = vertexX[j1];
			l = vertexY[j1];
			i1 = vertexZ[j1];
			if (anIntArray1655 != null) {
				anIntArray1655[j1] = stream_4.readUnsignedByte();
			}
		}
		stream.currentOffset = class21.anInt379;
		stream_1.currentOffset = class21.anInt380;
		stream_2.currentOffset = class21.anInt381;
		stream_3.currentOffset = class21.anInt382;
		stream_4.currentOffset = class21.anInt383;
		for (int l1 = 0; l1 < numberOfTriangleFaces; l1++) {
			face_color[l1] = stream.readUnsignedWord();
			if (face_render_type != null) {
				face_render_type[l1] = stream_1.readUnsignedByte();
			}
			if (face_render_priorities != null) {
				face_render_priorities[l1] = stream_2.readUnsignedByte();
			}
			if (anIntArray1639 != null) {
				anIntArray1639[l1] = stream_3.readUnsignedByte();
			}
			if (anIntArray1656 != null) {
				anIntArray1656[l1] = stream_4.readUnsignedByte();
			}
		}
		stream.currentOffset = class21.anInt377;
		stream_1.currentOffset = class21.anInt378;
		int j2 = 0;
		int l2 = 0;
		int j3 = 0;
		int k3 = 0;
		for (int l3 = 0; l3 < numberOfTriangleFaces; l3++) {
			int i4 = stream_1.readUnsignedByte();
			if (i4 == 1) {
				j2 = stream.method421() + k3;
				k3 = j2;
				l2 = stream.method421() + k3;
				k3 = l2;
				j3 = stream.method421() + k3;
				k3 = j3;
				face_a[l3] = j2;
				face_b[l3] = l2;
				face_c[l3] = j3;
			}
			if (i4 == 2) {
				//j2 = j2;
				l2 = j3;
				j3 = stream.method421() + k3;
				k3 = j3;
				face_a[l3] = j2;
				face_b[l3] = l2;
				face_c[l3] = j3;
			}
			if (i4 == 3) {
				j2 = j3;
				//l2 = l2;
				j3 = stream.method421() + k3;
				k3 = j3;
				face_a[l3] = j2;
				face_b[l3] = l2;
				face_c[l3] = j3;
			}
			if (i4 == 4) {
				int k4 = j2;
				j2 = l2;
				l2 = k4;
				j3 = stream.method421() + k3;
				k3 = j3;
				face_a[l3] = j2;
				face_b[l3] = l2;
				face_c[l3] = j3;
			}
		}
		stream.currentOffset = class21.anInt384;
		for (int j4 = 0; j4 < numberOfTexturesFaces; j4++) {
			textured_face_a[j4] = stream.readUnsignedWord();
			textured_face_b[j4] = stream.readUnsignedWord();
			textured_face_c[j4] = stream.readUnsignedWord();
		}
	}*/

  /*  private void readOldModel(int i) {
        int j = -870;
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        ModelHeader class21 = aClass21Array1661[i];
        verticesCount = class21.anInt369;
        trianglesCount = class21.anInt370;
        texturesCount = class21.anInt371;
        verticesX = new int[verticesCount];
        verticesY = new int[verticesCount];
        verticesZ = new int[verticesCount];
        trianglesX = new int[trianglesCount];
        trianglesY = new int[trianglesCount];
        while (j >= 0)
            aBoolean1618 = !aBoolean1618;
        trianglesZ = new int[trianglesCount];
        texturesX = new int[texturesCount];
        texturesY = new int[texturesCount];
        texturesZ = new int[texturesCount];
        if (class21.anInt376 >= 0)
            vertexData = new int[verticesCount];
        if (class21.anInt380 >= 0)
            types = new int[trianglesCount];
        if (class21.anInt381 >= 0)
            face_render_priorities = new int[trianglesCount];
        else
            face_priority = -class21.anInt381 - 1;
        if (class21.anInt382 >= 0)
            alphas = new int[trianglesCount];
        if (class21.anInt383 >= 0)
            triangleData = new int[trianglesCount];
        colors = new int[trianglesCount];
        Buffer stream = new Buffer(class21.aByteArray368);
        stream.currentOffset = class21.anInt372;
        Buffer stream_1 = new Buffer(class21.aByteArray368);
        stream_1.currentOffset = class21.anInt373;
        Buffer stream_2 = new Buffer(class21.aByteArray368);
        stream_2.currentOffset = class21.anInt374;
        Buffer stream_3 = new Buffer(class21.aByteArray368);
        stream_3.currentOffset = class21.anInt375;
        Buffer stream_4 = new Buffer(class21.aByteArray368);
        stream_4.currentOffset = class21.anInt376;
        int k = 0;
        int l = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < verticesCount; j1++) {
            int k1 = stream.readUnsignedByte();
            int i2 = 0;
            if ((k1 & 1) != 0)
                i2 = stream_1.method421();
            int k2 = 0;
            if ((k1 & 2) != 0)
                k2 = stream_2.method421();
            int i3 = 0;
            if ((k1 & 4) != 0)
                i3 = stream_3.method421();
            verticesX[j1] = k + i2;
            verticesY[j1] = l + k2;
            verticesZ[j1] = i1 + i3;
            k = verticesX[j1];
            l = verticesY[j1];
            i1 = verticesZ[j1];
            if (vertexData != null)
                vertexData[j1] = stream_4.readUnsignedByte();
        }
        stream.currentOffset = class21.anInt379;
        stream_1.currentOffset = class21.anInt380;
        stream_2.currentOffset = class21.anInt381;
        stream_3.currentOffset = class21.anInt382;
        stream_4.currentOffset = class21.anInt383;
        for (int l1 = 0; l1 < trianglesCount; l1++) {
            colors[l1] = stream.readUShort();
            if (types != null)
                types[l1] = stream_1.readUnsignedByte();
            if (face_render_priorities != null)
                face_render_priorities[l1] = stream_2.readUnsignedByte();
            if (alphas != null) {
                alphas[l1] = stream_3.readUnsignedByte();
            }
            if (triangleData != null)
                triangleData[l1] = stream_4.readUnsignedByte();
        }
        stream.currentOffset = class21.anInt377;
        stream_1.currentOffset = class21.anInt378;
        int j2 = 0;
        int coordinate_b = 0;
        int coordinate_c = 0;
        int k3 = 0;
        for (int l3 = 0; l3 < trianglesCount; l3++) {
            int opcode = stream_1.readUnsignedByte();
            if (opcode == 1) {
                j2 = stream.method421() + k3;
                k3 = j2;
                coordinate_b = stream.method421() + k3;
                k3 = coordinate_b;
                coordinate_c = stream.method421() + k3;
                k3 = coordinate_c;
                trianglesX[l3] = j2;
                trianglesY[l3] = coordinate_b;
                trianglesZ[l3] = coordinate_c;
            }
            if (opcode == 2) {
                coordinate_b = coordinate_c;
                coordinate_c = stream.method421() + k3;
                k3 = coordinate_c;
                trianglesX[l3] = j2;
                trianglesY[l3] = coordinate_b;
                trianglesZ[l3] = coordinate_c;
            }
            if (opcode == 3) {
                j2 = coordinate_c;
                coordinate_c = stream.method421() + k3;
                k3 = coordinate_c;
                trianglesX[l3] = j2;
                trianglesY[l3] = coordinate_b;
                trianglesZ[l3] = coordinate_c;
            }
            if (opcode == 4) {
                int k4 = j2;
                j2 = coordinate_b;
                coordinate_b = k4;
                coordinate_c = stream.method421() + k3;
                k3 = coordinate_c;
                trianglesX[l3] = j2;
                trianglesY[l3] = coordinate_b;
                trianglesZ[l3] = coordinate_c;
            }
        }
        stream.currentOffset = class21.anInt384;
        for (int j4 = 0; j4 < texturesCount; j4++) {
            texturesX[j4] = stream.readUShort();
            texturesY[j4] = stream.readUShort();
            texturesZ[j4] = stream.readUShort();
        }
    }*/

    public static void method460(byte abyte0[], int j) {
        try {
            if (abyte0 == null) {
                ModelHeader class21 = aClass21Array1661[j] = new ModelHeader();
                class21.anInt369 = 0;
                class21.anInt370 = 0;
                class21.anInt371 = 0;
                return;
            }
            Buffer stream = new Buffer(abyte0);
            stream.currentOffset = abyte0.length - 18;
            ModelHeader class21_1 = aClass21Array1661[j] = new ModelHeader();
            class21_1.aByteArray368 = abyte0;
            class21_1.anInt369 = stream.readUShort();
            class21_1.anInt370 = stream.readUShort();
            class21_1.anInt371 = stream.readUnsignedByte();
            int k = stream.readUnsignedByte();
            int l = stream.readUnsignedByte();
            int i1 = stream.readUnsignedByte();
            int j1 = stream.readUnsignedByte();
            int k1 = stream.readUnsignedByte();
            int l1 = stream.readUShort();
            int i2 = stream.readUShort();
            int j2 = stream.readUShort();
            int k2 = stream.readUShort();
            int l2 = 0;
            class21_1.anInt372 = l2;
            l2 += class21_1.anInt369;
            class21_1.anInt378 = l2;
            l2 += class21_1.anInt370;
            class21_1.anInt381 = l2;
            if (l == 255) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt381 = -l - 1;
            }
            class21_1.anInt383 = l2;
            if (j1 == 1) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt383 = -1;
            }
            class21_1.anInt380 = l2;
            if (k == 1) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt380 = -1;
            }
            class21_1.anInt376 = l2;
            if (k1 == 1) {
                l2 += class21_1.anInt369;
            } else {
                class21_1.anInt376 = -1;
            }
            class21_1.anInt382 = l2;
            if (i1 == 1) {
                l2 += class21_1.anInt370;
            } else {
                class21_1.anInt382 = -1;
            }
            class21_1.anInt377 = l2;
            l2 += k2;
            class21_1.anInt379 = l2;
            l2 += class21_1.anInt370 * 2;
            class21_1.anInt384 = l2;
            l2 += class21_1.anInt371 * 6;
            class21_1.anInt373 = l2;
            l2 += l1;
            class21_1.anInt374 = l2;
            l2 += i2;
            class21_1.anInt375 = l2;
            l2 += j2;
        } catch (Exception _ex) {
        }
    }

    public static boolean newmodel[];

    public static void method459(int i, OnDemandFetcherParent onDemandFetcherParent) {
        aClass21Array1661 = new ModelHeader[100_000];
        newmodel = new boolean[100000];
        aOnDemandFetcherParent_1662 = onDemandFetcherParent;
    }

    public static void method461(int j) {
        aClass21Array1661[j] = null;
    }

    public static Model method462(int j) {
        if (aClass21Array1661 == null) {
            return null;
        }
        ModelHeader class21 = aClass21Array1661[j];
        if (class21 == null) {
            aOnDemandFetcherParent_1662.method548(j);
            return null;
        } else {
            return new Model(j);
        }
    }

    public static boolean method463(int i) {
        if (aClass21Array1661 == null) {
            return false;
        }

        ModelHeader class21 = aClass21Array1661[i];
        if (class21 == null) {
            aOnDemandFetcherParent_1662.method548(i);
            return false;
        } else {
            return true;
        }
    }

    private Model(boolean flag) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        if (!flag) {
            aBoolean1618 = !aBoolean1618;
        }
    }

    public Model(int length, Model model_segments[]) {
        try {
            aBoolean1618 = true;
            fits_on_single_square = false;
            this.xMidOffset = -1;
            this.yMidOffset = -1;
            this.zMidOffset = -1;
            anInt1620++;
            boolean type_flag = false;
            boolean priority_flag = false;
            boolean alpha_flag = false;
            boolean tSkin_flag = false;
            boolean color_flag = false;
            boolean texture_flag = false;
            boolean coordinate_flag = false;
            verticesCount = 0;
            trianglesCount = 0;
            texturesCount = 0;
            face_priority = -1;
            Model build;
            for (int segment_index = 0; segment_index < length; segment_index++) {
                build = model_segments[segment_index];
                if (build != null) {
                    verticesCount += build.verticesCount;
                    trianglesCount += build.trianglesCount;
                    texturesCount += build.texturesCount;
                    type_flag |= build.types != null;
                    alpha_flag |= build.alphas != null;
                    if (build.face_render_priorities != null) {
                        priority_flag = true;
                    } else {
                        if (face_priority == -1)
                            face_priority = build.face_priority;

                        if (face_priority != build.face_priority)
                            priority_flag = true;
                    }
                    tSkin_flag |= build.triangleData != null;
                    color_flag |= build.colors != null;
                    texture_flag |= build.materials != null;
                    coordinate_flag |= build.textures != null;
                }
            }
            verticesX = new int[verticesCount];
            verticesY = new int[verticesCount];
            verticesZ = new int[verticesCount];
            vertexData = new int[verticesCount];
            trianglesX = new int[trianglesCount];
            trianglesY = new int[trianglesCount];
            trianglesZ = new int[trianglesCount];
            if(color_flag)
                colors = new short[trianglesCount];

            if (type_flag)
                types = new int[trianglesCount];

            if (priority_flag)
                face_render_priorities = new byte[trianglesCount];

            if (alpha_flag)
                alphas = new int[trianglesCount];

            if (tSkin_flag)
                triangleData = new int[trianglesCount];

            if(texture_flag)
                materials = new short[trianglesCount];

            if (coordinate_flag)
                textures = new byte[trianglesCount];

            if(texturesCount > 0) {
                textureTypes = new byte[texturesCount];
                texturesX = new short[texturesCount];
                texturesY = new short[texturesCount];
                texturesZ = new short[texturesCount];
            }
            verticesCount = 0;
            trianglesCount = 0;
            texturesCount = 0;
            int texture_face = 0;
            for (int segment_index = 0; segment_index < length; segment_index++) {
                build = model_segments[segment_index];
                if (build != null) {
                    for (int face = 0; face < build.trianglesCount; face++) {
                        if(type_flag && build.types != null)
                            types[trianglesCount] = build.types[face];

                        if (priority_flag)
                            if (build.face_render_priorities == null)
                                face_render_priorities[trianglesCount] = (byte) build.face_priority;
                            else
                                face_render_priorities[trianglesCount] = build.face_render_priorities[face];

                        if (alpha_flag && build.alphas != null)
                            alphas[trianglesCount] = build.alphas[face];

                        if (tSkin_flag && build.triangleData != null)
                            triangleData[trianglesCount] = build.triangleData[face];

                        if(texture_flag) {
                            if(build.materials != null)
                                materials[trianglesCount] = build.materials[face];
                            else
                                materials[trianglesCount] = -1;
                        }
                        if(coordinate_flag) {
                            if(build.textures != null && build.textures[face] != -1) {
                                textures[trianglesCount] = (byte) (build.textures[face] + texture_face);
                            } else {
                                textures[trianglesCount] = -1;
                            }
                        }
                        colors[trianglesCount] = build.colors[face];
                        trianglesX[trianglesCount] = method465(build, build.trianglesX[face]);
                        trianglesY[trianglesCount] = method465(build, build.trianglesY[face]);
                        trianglesZ[trianglesCount] = method465(build, build.trianglesZ[face]);
                        trianglesCount++;
                    }
                    for (int texture_edge = 0; texture_edge < build.texturesCount; texture_edge++) {
                        texturesX[texturesCount] = (short) method465(build, build.texturesX[texture_edge]);
                        texturesY[texturesCount] = (short) method465(build, build.texturesY[texture_edge]);
                        texturesZ[texturesCount] = (short) method465(build, build.texturesZ[texture_edge]);
                        texturesCount++;
                    }
                    texture_face += build.texturesCount;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Model(Model amodel[]) {
        int i = 2;
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        boolean texture_flag = false;
        boolean coordinate_flag = false;
        verticesCount = 0;
        trianglesCount = 0;
        texturesCount = 0;
        face_priority = -1;
        for (int k = 0; k < i; k++) {
            Model model = amodel[k];
            if (model != null) {
                verticesCount += model.verticesCount;
                trianglesCount += model.trianglesCount;
                texturesCount += model.texturesCount;
                flag1 |= model.types != null;
                if (model.face_render_priorities != null) {
                    flag2 = true;
                } else {
                    if (face_priority == -1)
                        face_priority = model.face_priority;
                    if (face_priority != model.face_priority)
                        flag2 = true;
                }
                flag3 |= model.alphas != null;
                flag4 |= model.colors != null;
                texture_flag |= model.materials != null;
                coordinate_flag |= model.textures != null;
            }
        }
        verticesX = new int[verticesCount];
        verticesY = new int[verticesCount];
        verticesZ = new int[verticesCount];
        trianglesX = new int[trianglesCount];
        trianglesY = new int[trianglesCount];
        trianglesZ = new int[trianglesCount];
        colorsX = new int[trianglesCount];
        colorsY = new int[trianglesCount];
        colorsZ = new int[trianglesCount];
        texturesX = new short[texturesCount];
        texturesY = new short[texturesCount];
        texturesZ = new short[texturesCount];
        if (flag1)
            types = new int[trianglesCount];
        if (flag2)
            face_render_priorities = new byte[trianglesCount];
        if (flag3)
            alphas = new int[trianglesCount];
        if (flag4)
            colors = new short[trianglesCount];
        if (texture_flag)
            materials = new short[trianglesCount];

        if (coordinate_flag)
            textures = new byte[trianglesCount];
        verticesCount = 0;
        trianglesCount = 0;
        texturesCount = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < i; j1++) {
            Model model_1 = amodel[j1];
            if (model_1 != null) {
                int k1 = verticesCount;
                for (int l1 = 0; l1 < model_1.verticesCount; l1++) {
                    int x = model_1.verticesX[l1];
                    int y = model_1.verticesY[l1];
                    int z = model_1.verticesZ[l1];
                    verticesX[verticesCount] = x;
                    verticesY[verticesCount] = y;
                    verticesZ[verticesCount] = z;
                    ++verticesCount;
                }

                for (int i2 = 0; i2 < model_1.trianglesCount; i2++) {
                    trianglesX[trianglesCount] = model_1.trianglesX[i2] + k1;
                    trianglesY[trianglesCount] = model_1.trianglesY[i2] + k1;
                    trianglesZ[trianglesCount] = model_1.trianglesZ[i2] + k1;
                    colorsX[trianglesCount] = model_1.colorsX[i2];
                    colorsY[trianglesCount] = model_1.colorsY[i2];
                    colorsZ[trianglesCount] = model_1.colorsZ[i2];
                    if (flag1)
                        if (model_1.types == null) {
                            types[trianglesCount] = 0;
                        } else {
                            int j2 = model_1.types[i2];
                            if ((j2 & 2) == 2)
                                j2 += i1 << 2;
                            types[trianglesCount] = j2;
                        }
                    if (flag2)
                        if (model_1.face_render_priorities == null)
                            face_render_priorities[trianglesCount] = (byte) model_1.face_priority;
                        else
                            face_render_priorities[trianglesCount] = model_1.face_render_priorities[i2];
                    if (flag3)
                        if (model_1.alphas == null)
                            alphas[trianglesCount] = 0;
                        else
                            alphas[trianglesCount] = model_1.alphas[i2];
                    if (flag4 && model_1.colors != null)
                        colors[trianglesCount] = model_1.colors[i2];

                    if (texture_flag) {
                        if (model_1.materials != null) {
                            materials[trianglesCount] = model_1.materials[trianglesCount];
                        } else {
                            materials[trianglesCount] = -1;
                        }
                    }

                    if (coordinate_flag) {
                        if (model_1.textures != null && model_1.textures[trianglesCount] != -1)
                            textures[trianglesCount] = (byte)(model_1.textures[trianglesCount] + texturesCount);
                        else
                            textures[trianglesCount] = -1;

                    }

                    trianglesCount++;
                }
                for (int k2 = 0; k2 < model_1.texturesCount; k2++) {
                    texturesX[texturesCount] = (short) (model_1.texturesX[k2] + k1);
                    texturesY[texturesCount] = (short) (model_1.texturesY[k2] + k1);
                    texturesZ[texturesCount] = (short) (model_1.texturesZ[k2] + k1);
                    texturesCount++;
                }
                i1 += model_1.texturesCount;
            }
        }
        calculateDistances();
    }
    public Model(boolean color_flag, boolean alpha_flag, boolean animated, Model model) {
        this(color_flag, alpha_flag, animated, false, model);
    }
    public Model(boolean color_flag, boolean alpha_flag, boolean animated, boolean texture_flag, Model model) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (animated) {
            verticesX = model.verticesX;
            verticesY = model.verticesY;
            verticesZ = model.verticesZ;
        } else {
            verticesX = new int[verticesCount];
            verticesY = new int[verticesCount];
            verticesZ = new int[verticesCount];
            for (int j = 0; j < verticesCount; j++) {
                verticesX[j] = model.verticesX[j];
                verticesY[j] = model.verticesY[j];
                verticesZ[j] = model.verticesZ[j];
            }

        }
        if (color_flag) {
            colors = model.colors;
        } else {
            colors = new short[trianglesCount];
            for (int k = 0; k < trianglesCount; k++) {
                colors[k] = model.colors[k];
            }

        }

        if(!texture_flag && model.materials != null) {
            materials = new short[trianglesCount];
            for(int face = 0; face < trianglesCount; face++) {
                materials[face] = model.materials[face];
            }
        } else {
            materials = model.materials;
        }

        if (alpha_flag) {
            alphas = model.alphas;
        } else {
            alphas = new int[trianglesCount];
            if (model.alphas == null) {
                for (int l = 0; l < trianglesCount; l++) {
                    alphas[l] = 0;
                }

            } else {
                for (int i1 = 0; i1 < trianglesCount; i1++) {
                    alphas[i1] = model.alphas[i1];
                }

            }
        }
        vertexData = model.vertexData;
        triangleData = model.triangleData;
        types = model.types;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        face_render_priorities = model.face_render_priorities;
        face_priority = model.face_priority;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
        textures = model.textures;
        textureTypes = model.textureTypes;
        vertexNormals = model.vertexNormals;
        faceNormals = model.faceNormals;
        vertexNormalsOffsets = model.vertexNormalsOffsets;
    }

    public Model(boolean flag, boolean flag1, Model model) {
        aBoolean1618 = true;
        fits_on_single_square = false;
        anInt1620++;
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (flag) {
            verticesY = new int[verticesCount];
            for (int j = 0; j < verticesCount; j++) {
                verticesY[j] = model.verticesY[j];
            }

        } else {
            verticesY = model.verticesY;
        }
//		if (flag1) {
//			colorsX = new int[trianglesCount];
//			colorsY = new int[trianglesCount];
//			colorsZ = new int[trianglesCount];
//			for (int k = 0; k < trianglesCount; k++) {
//				colorsX[k] = model.colorsX[k];
//				colorsY[k] = model.colorsY[k];
//				colorsZ[k] = model.colorsZ[k];
//			}
//
//			types = new int[trianglesCount];
//			if (model.types == null) {
//				for (int l = 0; l < trianglesCount; l++) {
//					types[l] = 0;
//				}
//
//			} else {
//				for (int i1 = 0; i1 < trianglesCount; i1++) {
//					types[i1] = model.types[i1];
//				}
//
//			}
//			super.vertexNormals = new VertexNormal[verticesCount];
//			for (int j1 = 0; j1 < verticesCount; j1++) {
//				VertexNormal class33 = super.vertexNormals[j1] = new VertexNormal();
//				VertexNormal class33_1 = model.vertexNormals[j1];
//				class33.x = class33_1.x;
//				class33.y = class33_1.y;
//				class33.z = class33_1.z;
//				class33.magnitude = class33_1.magnitude;
//			}
//
//			gouraudVertex = model.gouraudVertex;
//		} else {
        colorsX = model.colorsX;
        colorsY = model.colorsY;
        colorsZ = model.colorsZ;
        types = model.types;
//		}
        verticesX = model.verticesX;
        verticesZ = model.verticesZ;
        colors = model.colors;
        alphas = model.alphas;
        face_render_priorities = model.face_render_priorities;
        face_priority = model.face_priority;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
        super.modelHeight = model.modelHeight;
        textures = model.textures;
        materials = model.materials;
        XYZMag = model.XYZMag;
        diagonal3DAboveOrigin = model.diagonal3DAboveOrigin;
        maxRenderDepth = model.maxRenderDepth;
        minimumXVertex = model.minimumXVertex;
        maximumZVertex = model.maximumZVertex;
        minimumZVertex = model.minimumZVertex;
        maximumXVertex = model.maximumXVertex;
    }


    public void method464(Model model, boolean flag) {
        verticesCount = model.verticesCount;
        trianglesCount = model.trianglesCount;
        texturesCount = model.texturesCount;
        if (anIntArray1622.length < verticesCount) {
            anIntArray1622 = new int[verticesCount + 10000];
            anIntArray1623 = new int[verticesCount + 10000];
            anIntArray1624 = new int[verticesCount + 10000];
        }
        verticesX = anIntArray1622;
        verticesY = anIntArray1623;
        verticesZ = anIntArray1624;
        for (int k = 0; k < verticesCount; k++) {
            verticesX[k] = model.verticesX[k];
            verticesY[k] = model.verticesY[k];
            verticesZ[k] = model.verticesZ[k];
        }

        if (flag) {
            alphas = model.alphas;
        } else {
            if (anIntArray1625.length < trianglesCount) {
                anIntArray1625 = new int[trianglesCount + 100];
            }
            alphas = anIntArray1625;
            if (model.alphas == null) {
                for (int l = 0; l < trianglesCount; l++) {
                    alphas[l] = 0;
                }

            } else {
                for (int i1 = 0; i1 < trianglesCount; i1++) {
                    alphas[i1] = model.alphas[i1];
                }

            }
        }
        types = model.types;
        colors = model.colors;
        face_render_priorities = model.face_render_priorities;
        face_priority = model.face_priority;
        faceGroups = model.faceGroups;
        vertexGroups = model.vertexGroups;
        trianglesX = model.trianglesX;
        trianglesY = model.trianglesY;
        trianglesZ = model.trianglesZ;
        colorsX = model.colorsX;
        colorsY = model.colorsY;
        colorsZ = model.colorsZ;
        texturesX = model.texturesX;
        texturesY = model.texturesY;
        texturesZ = model.texturesZ;
        textures = model.textures;
        textureTypes = model.textureTypes;
        materials = model.materials;
        faceNormals = model.faceNormals;
        vertexNormalsOffsets = model.vertexNormalsOffsets;
    }

    private final int method465(Model model, int i) {
        int j = -1;
        int k = model.verticesX[i];
        int l = model.verticesY[i];
        int i1 = model.verticesZ[i];
        for (int j1 = 0; j1 < verticesCount; j1++) {
            if (k != verticesX[j1] || l != verticesY[j1] || i1 != verticesZ[j1]) {
                continue;
            }
            j = j1;
            break;
        }

        if (j == -1) {
            verticesX[verticesCount] = k;
            verticesY[verticesCount] = l;
            verticesZ[verticesCount] = i1;
            if (model.vertexData != null) {
                vertexData[verticesCount] = model.vertexData[i];
            }
            j = verticesCount++;
        }
        return j;
    }

    public void calculateDistances() {
        super.modelHeight = 0;
        XYZMag = 0;
        maximumYVertex = 0;
        for (int i = 0; i < verticesCount; i++) {
            int j = verticesX[i];
            int k = verticesY[i];
            int l = verticesZ[i];
            if (-k > super.modelHeight) {
                super.modelHeight = -k;
            }
            if (k > maximumYVertex) {
                maximumYVertex = k;
            }
            int i1 = j * j + l * l;
            if (i1 > XYZMag) {
                XYZMag = i1;
            }
        }
        XYZMag = (int) (Math.sqrt(XYZMag) + 0.98999999999999999D);
        diagonal3DAboveOrigin = (int) (Math.sqrt(XYZMag * XYZMag + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
        maxRenderDepth = diagonal3DAboveOrigin + (int) (Math.sqrt(XYZMag * XYZMag + maximumYVertex * maximumYVertex) + 0.98999999999999999D);
    }

    public void method467() {
        super.modelHeight = 0;
        maximumYVertex = 0;
        for (int i = 0; i < verticesCount; i++) {
            int j = verticesY[i];
            if (-j > super.modelHeight) {
                super.modelHeight = -j;
            }
            if (j > maximumYVertex) {
                maximumYVertex = j;
            }
        }

        diagonal3DAboveOrigin = (int) (Math.sqrt(XYZMag * XYZMag + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
        maxRenderDepth = diagonal3DAboveOrigin + (int) (Math.sqrt(XYZMag * XYZMag + maximumYVertex * maximumYVertex) + 0.98999999999999999D);
    }

    public void method468(int i) {
        super.modelHeight = 0;
        XYZMag = 0;
        maximumYVertex = 0;
        minimumXVertex = 0xf423f;
        maximumXVertex = 0xfff0bdc1;
        maximumZVertex = 0xfffe7961;
        minimumZVertex = 0x1869f;
        for (int j = 0; j < verticesCount; j++) {
            int k = verticesX[j];
            int l = verticesY[j];
            int i1 = verticesZ[j];
            if (k < minimumXVertex) {
                minimumXVertex = k;
            }
            if (k > maximumXVertex) {
                maximumXVertex = k;
            }
            if (i1 < minimumZVertex) {
                minimumZVertex = i1;
            }
            if (i1 > maximumZVertex) {
                maximumZVertex = i1;
            }
            if (-l > super.modelHeight) {
                super.modelHeight = -l;
            }
            if (l > maximumYVertex) {
                maximumYVertex = l;
            }
            int j1 = k * k + i1 * i1;
            if (j1 > XYZMag) {
                XYZMag = j1;
            }
        }

        XYZMag = (int) Math.sqrt(XYZMag);
        diagonal3DAboveOrigin = (int) Math.sqrt(XYZMag * XYZMag + super.modelHeight * super.modelHeight);
        if (i != 21073) {
            return;
        } else {
            maxRenderDepth = diagonal3DAboveOrigin + (int) Math.sqrt(XYZMag * XYZMag + maximumYVertex * maximumYVertex);
            return;
        }
    }

    public void method469() {
        if (vertexData != null) {
            int ai[] = new int[256];
            int j = 0;
            for (int l = 0; l < verticesCount; l++) {
                int j1 = vertexData[l];
                ai[j1]++;
                if (j1 > j) {
                    j = j1;
                }
            }

            vertexGroups = new int[j + 1][];
            for (int k1 = 0; k1 <= j; k1++) {
                vertexGroups[k1] = new int[ai[k1]];
                ai[k1] = 0;
            }

            for (int j2 = 0; j2 < verticesCount; j2++) {
                int l2 = vertexData[j2];
                vertexGroups[l2][ai[l2]++] = j2;
            }

            vertexData = null;
        }
        if (triangleData != null) {
            int ai1[] = new int[256];
            int k = 0;
            for (int i1 = 0; i1 < trianglesCount; i1++) {
                int l1 = triangleData[i1];
                ai1[l1]++;
                if (l1 > k) {
                    k = l1;
                }
            }

            faceGroups = new int[k + 1][];
            for (int i2 = 0; i2 <= k; i2++) {
                faceGroups[i2] = new int[ai1[i2]];
                ai1[i2] = 0;
            }

            for (int k2 = 0; k2 < trianglesCount; k2++) {
                int i3 = triangleData[k2];
                faceGroups[i3][ai1[i3]++] = k2;
            }

            triangleData = null;
        }
    }

    public void method470(int i) {
        if (vertexGroups == null) {
            return;
        }
        if (i == -1) {
            return;
        }
        Class36 class36 = Class36.forId(i);
        if (class36 == null) {
            return;
        }
        Class18 class18 = class36.aClass18_637;
        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        for (int k = 0; k < class36.anInt638; k++) {
            int l = class36.anIntArray639[k];
            method472(class18.anIntArray342[l], class18.anIntArray343[l], class36.anIntArray640[k], class36.anIntArray641[k], class36.anIntArray642[k]);
        }

    }

    public void method471(int ai[], int j, int k) {
        if (k == -1) {
            return;
        }
        if (ai == null || j == -1) {
            method470(k);
            return;
        }
        Class36 class36 = Class36.forId(k);
        if (class36 == null) {
            return;
        }
        Class36 class36_1 = Class36.forId(j);
        if (class36_1 == null) {
            method470(k);
            return;
        }
        Class18 class18 = class36.aClass18_637;
        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        int l = 0;
        int i1 = ai[l++];
        for (int j1 = 0; j1 < class36.anInt638; j1++) {
            int k1;
            for (k1 = class36.anIntArray639[j1]; k1 > i1; i1 = ai[l++]) {
                ;
            }
            if (k1 != i1 || class18.anIntArray342[k1] == 0) {
                method472(class18.anIntArray342[k1], class18.anIntArray343[k1], class36.anIntArray640[j1], class36.anIntArray641[j1], class36.anIntArray642[j1]);
            }
        }

        xAnimOffset = 0;
        yAnimOffset = 0;
        zAnimOffset = 0;
        l = 0;
        i1 = ai[l++];
        for (int l1 = 0; l1 < class36_1.anInt638; l1++) {
            int i2;
            for (i2 = class36_1.anIntArray639[l1]; i2 > i1; i1 = ai[l++]) {
                ;
            }
            if (i2 == i1 || class18.anIntArray342[i2] == 0) {
                method472(class18.anIntArray342[i2], class18.anIntArray343[i2], class36_1.anIntArray640[l1], class36_1.anIntArray641[l1], class36_1.anIntArray642[l1]);
            }
        }

    }

    private void method472(int i, int ai[], int j, int k, int l) {

        int i1 = ai.length;
        if (i == 0) {
            int j1 = 0;
            xAnimOffset = 0;
            yAnimOffset = 0;
            zAnimOffset = 0;
            for (int k2 = 0; k2 < i1; k2++) {
                int l3 = ai[k2];
                if (l3 < vertexGroups.length) {
                    int ai5[] = vertexGroups[l3];
                    for (int j6 : ai5) {
                        xAnimOffset += verticesX[j6];
                        yAnimOffset += verticesY[j6];
                        zAnimOffset += verticesZ[j6];
                        j1++;
                    }

                }
            }

            if (j1 > 0) {
                xAnimOffset = xAnimOffset / j1 + j;
                yAnimOffset = yAnimOffset / j1 + k;
                zAnimOffset = zAnimOffset / j1 + l;
                return;
            } else {
                xAnimOffset = j;
                yAnimOffset = k;
                zAnimOffset = l;
                return;
            }
        }
        if (i == 1) {
            for (int k1 = 0; k1 < i1; k1++) {
                int l2 = ai[k1];
                if (l2 < vertexGroups.length) {
                    int ai1[] = vertexGroups[l2];
                    for (int element : ai1) {
                        int j5 = element;
                        verticesX[j5] += j;
                        verticesY[j5] += k;
                        verticesZ[j5] += l;
                    }

                }
            }

            return;
        }
        if (i == 2) {
            for (int l1 = 0; l1 < i1; l1++) {
                int i3 = ai[l1];
                if (i3 < vertexGroups.length) {
                    int ai2[] = vertexGroups[i3];
                    for (int element : ai2) {
                        int k5 = element;
                        verticesX[k5] -= xAnimOffset;
                        verticesY[k5] -= yAnimOffset;
                        verticesZ[k5] -= zAnimOffset;
                        int k6 = (j & 0xff) * 8;
                        int l6 = (k & 0xff) * 8;
                        int i7 = (l & 0xff) * 8;
                        if (i7 != 0) {
                            int j7 = SINE[i7];
                            int i8 = COSINE[i7];
                            int l8 = verticesY[k5] * j7 + verticesX[k5] * i8 >> 16;
                            verticesY[k5] = verticesY[k5] * i8 - verticesX[k5] * j7 >> 16;
                            verticesX[k5] = l8;
                        }
                        if (k6 != 0) {
                            int k7 = SINE[k6];
                            int j8 = COSINE[k6];
                            int i9 = verticesY[k5] * j8 - verticesZ[k5] * k7 >> 16;
                            verticesZ[k5] = verticesY[k5] * k7 + verticesZ[k5] * j8 >> 16;
                            verticesY[k5] = i9;
                        }
                        if (l6 != 0) {
                            int l7 = SINE[l6];
                            int k8 = COSINE[l6];
                            int j9 = verticesZ[k5] * l7 + verticesX[k5] * k8 >> 16;
                            verticesZ[k5] = verticesZ[k5] * k8 - verticesX[k5] * l7 >> 16;
                            verticesX[k5] = j9;
                        }
                        verticesX[k5] += xAnimOffset;
                        verticesY[k5] += yAnimOffset;
                        verticesZ[k5] += zAnimOffset;
                    }

                }
            }
            return;
        }
        if (i == 3) {
            for (int i2 = 0; i2 < i1; i2++) {
                int j3 = ai[i2];
                if (j3 < vertexGroups.length) {
                    int ai3[] = vertexGroups[j3];
                    for (int element : ai3) {
                        int l5 = element;
                        verticesX[l5] -= xAnimOffset;
                        verticesY[l5] -= yAnimOffset;
                        verticesZ[l5] -= zAnimOffset;
                        verticesX[l5] = verticesX[l5] * j / 128;
                        verticesY[l5] = verticesY[l5] * k / 128;
                        verticesZ[l5] = verticesZ[l5] * l / 128;
                        verticesX[l5] += xAnimOffset;
                        verticesY[l5] += yAnimOffset;
                        verticesZ[l5] += zAnimOffset;
                    }
                }
            }
            return;
        }
        if (i == 5 && faceGroups != null && alphas != null) {
            for (int j2 = 0; j2 < i1; j2++) {
                int k3 = ai[j2];
                if (k3 < faceGroups.length) {
                    int ai4[] = faceGroups[k3];
                    for (int element : ai4) {
                        int i6 = element;
                        alphas[i6] += j * 8;
                        if (alphas[i6] < 0) {
                            alphas[i6] = 0;
                        }
                        if (alphas[i6] > 255) {
                            alphas[i6] = 255;
                        }
                    }
                }
            }
        }
    }

    public void method473() {
        for (int j = 0; j < verticesCount; j++) {
            int k = verticesX[j];
            verticesX[j] = verticesZ[j];
            verticesZ[j] = -k;
        }
    }

    public void method474(int i) {
        int k = SINE[i];
        int l = COSINE[i];
        for (int i1 = 0; i1 < verticesCount; i1++) {
            int j1 = verticesY[i1] * l - verticesZ[i1] * k >> 16;
            verticesZ[i1] = verticesY[i1] * k + verticesZ[i1] * l >> 16;
            verticesY[i1] = j1;
        }
    }

    public void method475(int i, int j, int l) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] += i;
            verticesY[i1] += j;
            verticesZ[i1] += l;
        }
    }

    public void replaceColor(int found, int replace) {
        if(colors != null)
            for (int face = 0; face < trianglesCount; face++)
                if (colors[face] == (short) found)
                    colors[face] = (short) replace;
    }

    public void replaceTexture(short found, short replace) {
        if(materials != null)
            for (int face = 0; face < trianglesCount; face++)
                if (materials[face] == found)
                    materials[face] = replace;
    }

    public void method477() {
        for (int j = 0; j < verticesCount; j++) {
            verticesZ[j] = -verticesZ[j];
        }
        for (int k = 0; k < trianglesCount; k++) {
            int l = trianglesX[k];
            trianglesX[k] = trianglesZ[k];
            trianglesZ[k] = l;
        }
    }

    public void method478(int i, int j, int l) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] = verticesX[i1] * i / 128;
            verticesY[i1] = verticesY[i1] * l / 128;
            verticesZ[i1] = verticesZ[i1] * j / 128;
        }

    }

    public final void method479(int i, int j, int k, int l, int i1, boolean flag) {
        int j1 = (int) Math.sqrt(k * k + l * l + i1 * i1);
        int k1 = j * j1 >> 8;
        if (colorsX == null) {
            colorsX = new int[trianglesCount];
            colorsY = new int[trianglesCount];
            colorsZ = new int[trianglesCount];
        }
        if (super.aClass33Array1425 == null) {
            super.aClass33Array1425 = new VertexNormal[verticesCount];
            for (int l1 = 0; l1 < verticesCount; l1++) {
                super.aClass33Array1425[l1] = new VertexNormal();
            }

        }
        for (int i2 = 0; i2 < trianglesCount; i2++) {
            int var17;
            if (types == null) {
                var17 = 0;
            } else {
                var17 = types[i2];
            }

            final int var18;
            if (alphas == null) {
                var18 = 0;
            } else {
                var18 = alphas[i2];
            }

            final short var12;
            if (materials == null) {
                var12 = -1;
            } else {
                var12 = materials[i2];
            }

            int j2 = trianglesX[i2];
            int l2 = trianglesY[i2];
            int i3 = trianglesZ[i2];
            int j3 = verticesX[l2] - verticesX[j2];
            int k3 = verticesY[l2] - verticesY[j2];
            int l3 = verticesZ[l2] - verticesZ[j2];
            int i4 = verticesX[i3] - verticesX[j2];
            int j4 = verticesY[i3] - verticesY[j2];
            int k4 = verticesZ[i3] - verticesZ[j2];
            int l4 = k3 * k4 - j4 * l3;
            int i5 = l3 * i4 - k4 * j3;
            int j5;
            for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192 || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
                l4 >>= 1;
                i5 >>= 1;
            }

            int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
            if (k5 <= 0) {
                k5 = 1;
            }
            l4 = l4 * 256 / k5;
            i5 = i5 * 256 / k5;
            j5 = j5 * 256 / k5;

            if (types == null || (types[i2] & 1) == 0) {

                VertexNormal class33_2 = super.aClass33Array1425[j2];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
                class33_2 = super.aClass33Array1425[l2];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;
                class33_2 = super.aClass33Array1425[i3];
                class33_2.x += l4;
                class33_2.y += i5;
                class33_2.z += j5;
                class33_2.magnitude++;

            } else {

                int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
                colorsX[i2] = method481(colors[i2], l5, types[i2]);

            }
        }

        if (flag) {
            method480(i, k1, k, l, i1);
        } else {
            vertexNormals = new VertexNormal[verticesCount];
            for (int k2 = 0; k2 < verticesCount; k2++) {
                VertexNormal class33 = super.aClass33Array1425[k2];
                VertexNormal class33_1 = vertexNormals[k2] = new VertexNormal();
                class33_1.x = class33.x;
                class33_1.y = class33.y;
                class33_1.z = class33.z;
                class33_1.magnitude = class33.magnitude;
            }

        }
        if (flag) {
            calculateDistances();
            return;
        } else {
            method468(21073);
            return;
        }
    }
    public void light() {
        if (vertexNormals == null) {
            vertexNormals = new VertexNormal[verticesCount];

            int var1;
            for (var1 = 0; var1 < verticesCount; ++var1) {
                vertexNormals[var1] = new VertexNormal();
            }

            for (var1 = 0; var1 < trianglesCount; ++var1) {
                final int var2 = trianglesX[var1];
                final int var3 = trianglesY[var1];
                final int var4 = trianglesZ[var1];
                final int var5 = verticesX[var3] - verticesX[var2];
                final int var6 = verticesY[var3] - verticesY[var2];
                final int var7 = verticesZ[var3] - verticesZ[var2];
                final int var8 = verticesX[var4] - verticesX[var2];
                final int var9 = verticesY[var4] - verticesY[var2];
                final int var10 = verticesZ[var4] - verticesZ[var2];
                int var11 = var6 * var10 - var9 * var7;
                int var12 = var7 * var8 - var10 * var5;

                int var13;
                for (var13 = var5 * var9 - var8 * var6; var11 > 8192 || var12 > 8192 || var13 > 8192 || var11 < -8192 || var12 < -8192 || var13 < -8192; var13 >>= 1) {
                    var11 >>= 1;
                    var12 >>= 1;
                }

                int var14 = (int)Math.sqrt((double)(var11 * var11 + var12 * var12 + var13 * var13)); // L: 1368
                if (var14 <= 0) {
                    var14 = 1;
                }

                var11 = var11 * 256 / var14;
                var12 = var12 * 256 / var14;
                var13 = var13 * 256 / var14;
                final int var15;
                if (types == null) {
                    var15 = 0;
                } else {
                    var15 = types[var1];
                }

                if (var15 == 0) {
                    VertexNormal var16 = vertexNormals[var2];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                    var16 = vertexNormals[var3];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                    var16 = vertexNormals[var4];
                    var16.x += var11;
                    var16.y += var12;
                    var16.z += var13;
                    ++var16.magnitude;
                } else if (var15 == 1) {
                    if (faceNormals == null) {
                        faceNormals = new FaceNormal[trianglesCount];
                    }

                    final FaceNormal var17 = faceNormals[var1] = new FaceNormal();
                    var17.x = var11;
                    var17.y = var12;
                    var17.z = var13;
                }
            }
        }
    }
    public void light(final int ambient, final int contrast, final int x, final int y, final int z, final boolean flag) {

        light();
        final int magnitude = (int) Math.sqrt((double) (x * x + y * y + z * z));
        final int k1 = contrast * magnitude >> 8;
        colorsX = new int[trianglesCount];
        colorsY = new int[trianglesCount];
        colorsZ = new int[trianglesCount];

        for (int var16 = 0; var16 < trianglesCount; ++var16) {
            int var17;
            if (types == null) {
                var17 = 0;
            } else {
                var17 = types[var16];
            }

            final int var18;
            if (alphas == null) {
                var18 = 0;
            } else {
                var18 = alphas[var16];
            }

            final short var12;
            if (materials == null) {
                var12 = -1;
            } else {
                var12 = materials[var16];
            }

            if (var18 == -2) {
                var17 = 3;
            }

            if (var18 == -1) {
                var17 = 2;
            }

            VertexNormal var13;
            int var14;
            final FaceNormal var19;
            if (var12 == -1) {
                if (var17 == 0) {
                    final int var15 = colors[var16];
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesX[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesX[var16]];
                    } else {
                        var13 = vertexNormals[trianglesX[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsX[var16] = method2792(var15, var14);
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesY[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesY[var16]];
                    } else {
                        var13 = vertexNormals[trianglesY[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsY[var16] = method2792(var15, var14);
                    if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesZ[var16]] != null) {
                        var13 = vertexNormalsOffsets[trianglesZ[var16]];
                    } else {
                        var13 = vertexNormals[trianglesZ[var16]];
                    }

                    var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                    colorsZ[var16] = method2792(var15, var14);
                } else if (var17 == 1) {
                    var19 = faceNormals[var16];
                    var14 = (y * var19.y + z * var19.z + x * var19.x) / (k1 / 2 + k1) + ambient;
                    colorsX[var16] = method2792(colors[var16], var14);
                    colorsZ[var16] = -1;
                } else if (var17 == 3) {
                    colorsX[var16] = 128;
                    colorsZ[var16] = -1;
                } else {
                    colorsZ[var16] = -2;
                }
            } else if (var17 == 0) {
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesX[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesX[var16]];
                } else {
                    var13 = vertexNormals[trianglesX[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsX[var16] = method2820(var14);
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesY[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesY[var16]];
                } else {
                    var13 = vertexNormals[trianglesY[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsY[var16] = method2820(var14);
                if (vertexNormalsOffsets != null && vertexNormalsOffsets[trianglesZ[var16]] != null) {
                    var13 = vertexNormalsOffsets[trianglesZ[var16]];
                } else {
                    var13 = vertexNormals[trianglesZ[var16]];
                }

                var14 = (y * var13.y + z * var13.z + x * var13.x) / (k1 * var13.magnitude) + ambient;
                colorsZ[var16] = method2820(var14);
            } else if (var17 == 1) {
                var19 = faceNormals[var16];
                var14 = (y * var19.y + z * var19.z + x * var19.x) / (k1 / 2 + k1) + ambient;
                colorsX[var16] = method2820(var14);
                colorsZ[var16] = -1;
            } else {
                colorsZ[var16] = -2;
            }
        }
        calculateDistances();
          if (textures == null) {
              method468(21073);
          }

    }
    public static String ccString = "Cla";
    public static String xxString = "at Cl";
    public static String vvString = "nt";
    public static String aString9_9 = "" + ccString + "n Ch" + xxString + "ie" + vvString + " ";
    private int method2792(final int var0, int var1) {
        var1 = (var0 & 127) * var1 >> 7;
        if (var1 < 2) {
            var1 = 2;
        } else if (var1 > 126) {
            var1 = 126;
        }

        return (var0 & '\uff80') + var1;
    }
    private int method2820(int var0) {
        if (var0 < 2) {
            var0 = 2;
        } else if (var0 > 126) {
            var0 = 126;
        }

        return var0;
    }
    public final void method480(int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < trianglesCount; j1++) {
            int k1 = trianglesX[j1];
            int i2 = trianglesY[j1];
            int j2 = trianglesZ[j1];
            if (types == null) {
                int i3 = colors[j1];
                VertexNormal class33 = super.aClass33Array1425[k1];
                int k2 = i + (k * class33.x + l * class33.y + i1 * class33.z) / (j * class33.magnitude);
                colorsX[j1] = method481(i3, k2, 0);
                class33 = super.aClass33Array1425[i2];
                k2 = i + (k * class33.x + l * class33.y + i1 * class33.z) / (j * class33.magnitude);
                colorsY[j1] = method481(i3, k2, 0);
                class33 = super.aClass33Array1425[j2];
                k2 = i + (k * class33.x + l * class33.y + i1 * class33.z) / (j * class33.magnitude);
                colorsZ[j1] = method481(i3, k2, 0);
            } else if ((types[j1] & 1) == 0) {
                int j3 = colors[j1];
                int k3 = types[j1];
                VertexNormal class33_1 = super.aClass33Array1425[k1];
                int l2 = i + (k * class33_1.x + l * class33_1.y + i1 * class33_1.z) / (j * class33_1.magnitude);
                colorsX[j1] = method481(j3, l2, k3);
                class33_1 = super.aClass33Array1425[i2];
                l2 = i + (k * class33_1.x + l * class33_1.y + i1 * class33_1.z) / (j * class33_1.magnitude);
                colorsY[j1] = method481(j3, l2, k3);
                class33_1 = super.aClass33Array1425[j2];
                l2 = i + (k * class33_1.x + l * class33_1.y + i1 * class33_1.z) / (j * class33_1.magnitude);
                colorsZ[j1] = method481(j3, l2, k3);
            }
        }

        super.aClass33Array1425 = null;
        vertexNormals = null;
        vertexData = null;
        triangleData = null;
        if (types != null) {
            for (int l1 = 0; l1 < trianglesCount; l1++) {
                if ((types[l1] & 2) == 2) {
                    return;
                }
            }

        }
        colors = null;
    }

    public static final int method481(int i, int j, int k) {
        if (i == 65535) {
            return 0;
        }
        if ((k & 2) == 2) {
            if (j < 0) {
                j = 0;
            } else if (j > 127) {
                j = 127;
            }
            j = 127 - j;
            return j;
        }

        j = j * (i & 0x7f) >> 7;
        if (j < 2) {
            j = 2;
        } else if (j > 126) {
            j = 126;
        }
        return (i & 0xff80) + j;
    }

    // method482
    public final void render(int j, int k, int l, int i1, int j1, int k1) {
        int i = 0;
        int l1 = Rasterizer.textureInt1;
        int i2 = Rasterizer.textureInt2;
        int j2 = SINE[i];
        int k2 = COSINE[i];
        int l2 = SINE[j];
        int i3 = COSINE[j];
        int j3 = SINE[k];
        int k3 = COSINE[k];
        int l3 = SINE[l];
        int i4 = COSINE[l];
        int j4 = j1 * l3 + k1 * i4 >> 16;
        for (int k4 = 0; k4 < verticesCount; k4++) {
            int l4 = verticesX[k4];
            int i5 = verticesY[k4];
            int j5 = verticesZ[k4];
            if (k != 0) {
                int k5 = i5 * j3 + l4 * k3 >> 16;
                i5 = i5 * k3 - l4 * j3 >> 16;
                l4 = k5;
            }
            if (i != 0) {
                int l5 = i5 * k2 - j5 * j2 >> 16;
                j5 = i5 * j2 + j5 * k2 >> 16;
                i5 = l5;
            }
            if (j != 0) {
                int i6 = j5 * l2 + l4 * i3 >> 16;
                j5 = j5 * i3 - l4 * l2 >> 16;
                l4 = i6;
            }
            l4 += i1;
            i5 += j1;
            j5 += k1;
            int j6 = i5 * i4 - j5 * l3 >> 16;
            j5 = i5 * l3 + j5 * i4 >> 16;
            i5 = j6;
            projected_verticesZ[k4] = j5 - j4;
            projected_verticesX[k4] = l1 + (l4 * WorldController.focalLength) / j5;
            projected_verticesY[k4] = i2 + (i5 * WorldController.focalLength) / j5;
            if (Rasterizer.saveDepth) {
                camera_verticesZ[k4] = j5;
            }
            if (texturesCount > 0) {
                anIntArray1668[k4] = l4;
                camera_verticesY[k4] = i5;
                viewportTextureZ[k4] = j5;
            }
        }

        try {
            method483(false, false, 0);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    /**
     * Entity / object render at point
     *
     * @param i
     * @param j
     * @param k
     * @param l
     * @param i1
     * @param j1
     * @param k1
     * @param l1
     * @param i2
     */
    @Override
    public final void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, long i2) {
        calculateBoundingBox(i);
        int j2 = l1 * i1 - j1 * l >> 16;
        int k2 = k1 * j + j2 * k >> 16;
        int l2 = XYZMag * k >> 16;
        int i3 = k2 + l2;
        if (i3 <= 50 || k2 >= 9500) {
            return;
        }
        int j3 = l1 * l + j1 * i1 >> 16;
        int k3 = (j3 - XYZMag) * WorldController.focalLength;
        if (k3 / i3 >= DrawingArea.centerY) {
            return;
        }
        int l3 = (j3 + XYZMag) * WorldController.focalLength;
        if (l3 / i3 <= -DrawingArea.centerY) {
            return;
        }
        int i4 = k1 * k - j2 * j >> 16;
        int j4 = XYZMag * j >> 16;
        int k4 = (i4 + j4) * WorldController.focalLength;
        if (k4 / i3 <= -DrawingArea.anInt1387) {
            return;
        }
        int l4 = j4 + (super.modelHeight * k >> 16);
        int i5 = (i4 - l4) * WorldController.focalLength;
        if (i5 / i3 >= DrawingArea.anInt1387) {
            return;
        }
        int j5 = l2 + (super.modelHeight * j >> 16);
        boolean flag = false;
        if (k2 - j5 <= 50) {
            flag = true;
        }
        boolean flag1 = false;
        if (i2 > 0 && aBoolean1684) {
            int k5 = k2 - l2;
            if (k5 <= 50) {
                k5 = 50;
            }
            if (j3 > 0) {
                k3 /= i3;
                l3 /= k5;
            } else {
                l3 /= i3;
                k3 /= k5;
            }
            if (i4 > 0) {
                i5 /= i3;
                k4 /= k5;
            } else {
                k4 /= i3;
                i5 /= k5;
            }
            int i6 = anInt1685 - Rasterizer.textureInt1;
            int k6 = anInt1686 - Rasterizer.textureInt2;
            if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4) {
                if (fits_on_single_square) {
                    obj_key[obj_loaded++] = i2;
                } else {
                    flag1 = true;
                }
            }
        }
        int l5 = Rasterizer.textureInt1;
        int j6 = Rasterizer.textureInt2;
        int l6 = 0;
        int i7 = 0;
        if (i != 0) {
            l6 = SINE[i];
            i7 = COSINE[i];
        }
        for (int j7 = 0; j7 < verticesCount; j7++) {
            int k7 = verticesX[j7];
            int l7 = verticesY[j7];
            int i8 = verticesZ[j7];
            if (i != 0) {
                int j8 = i8 * l6 + k7 * i7 >> 16;
                i8 = i8 * i7 - k7 * l6 >> 16;
                k7 = j8;
            }
            k7 += j1;
            l7 += k1;
            i8 += l1;
            int k8 = i8 * l + k7 * i1 >> 16;
            i8 = i8 * i1 - k7 * l >> 16;
            k7 = k8;
            k8 = l7 * k - i8 * j >> 16;
            i8 = l7 * j + i8 * k >> 16;
            l7 = k8;
            projected_verticesZ[j7] = i8 - k2;
            if (i8 >= 50) {
                projected_verticesX[j7] = (l5 + k7 * WorldController.focalLength
                        / i8);
                projected_verticesY[j7] = (j6 + l7 * WorldController.focalLength
                        / i8);
                if (Rasterizer.saveDepth) {
                    camera_verticesZ[j7] = i8;
                }
            } else {
                projected_verticesX[j7] = -5000;
                flag = true;
            }
            if (flag || texturesCount > 0) {
                anIntArray1668[j7] = k7;
                camera_verticesY[j7] = l7;
                viewportTextureZ[j7] = i8;
            }
        }

        try {
            method483(flag, flag1, i2);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    private final void method483(boolean flag, boolean flag1, long i22) {
        for (int j = 0; j < maxRenderDepth; j++)
            depthListIndices[j] = 0;

        for (int k = 0; k < trianglesCount; k++)
            if (types == null || types[k] != -1) {
                int l = trianglesX[k];
                int k1 = trianglesY[k];
                int j2 = trianglesZ[k];
                int i3 = projected_verticesX[l];
                int l3 = projected_verticesX[k1];
                int k4 = projected_verticesX[j2];
                if (flag && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
                    outOfReach[k] = true;
                    int j5 = (projected_verticesZ[l] + projected_verticesZ[k1] + projected_verticesZ[j2])
                            / 3 + diagonal3DAboveOrigin;
                    faceLists[j5][depthListIndices[j5]++] = k;
                } else {
                    if (flag1
                            && method486(anInt1685, anInt1686,
                            projected_verticesY[l],
                            projected_verticesY[k1],
                            projected_verticesY[j2], i3, l3, k4)) {
                        obj_key[obj_loaded++] = i22;
                        flag1 = false;
                    }
                    if ((i3 - l3)
                            * (projected_verticesY[j2] - projected_verticesY[k1])
                            - (projected_verticesY[l] - projected_verticesY[k1])
                            * (k4 - l3) > 0) {
                        outOfReach[k] = false;
                        if (i3 < 0 || l3 < 0 || k4 < 0
                                || i3 > DrawingArea.centerX
                                || l3 > DrawingArea.centerX
                                || k4 > DrawingArea.centerX)
                            hasAnEdgeToRestrict[k] = true;
                        else
                            hasAnEdgeToRestrict[k] = false;
                        int k5 = (projected_verticesZ[l] + projected_verticesZ[k1] + projected_verticesZ[j2])
                                / 3 + diagonal3DAboveOrigin;
                        faceLists[k5][depthListIndices[k5]++] = k;
                    }
                }
            }

        if (face_render_priorities == null) {
            for (int i1 = maxRenderDepth - 1; i1 >= 0; i1--) {
                int l1 = depthListIndices[i1];
                if (l1 > 0) {
                    int ai[] = faceLists[i1];
                    for (int j3 = 0; j3 < l1; j3++)
                        method484(ai[j3]);

                }
            }

            return;
        }
        for (int j1 = 0; j1 < 12; j1++) {
            anIntArray1673[j1] = 0;
            anIntArray1677[j1] = 0;
        }

        for (int i2 = maxRenderDepth - 1; i2 >= 0; i2--) {
            int k2 = depthListIndices[i2];
            if (k2 > 0) {
                int ai1[] = faceLists[i2];
                for (int i4 = 0; i4 < k2; i4++) {
                    int l4 = ai1[i4];
                    int l5 = face_render_priorities[l4];
                    int j6 = anIntArray1673[l5]++;
                    anIntArrayArray1674[l5][j6] = l4;
                    if (l5 < 10)
                        anIntArray1677[l5] += i2;
                    else if (l5 == 10)
                        anIntArray1675[j6] = i2;
                    else
                        anIntArray1676[j6] = i2;
                }

            }
        }

        int l2 = 0;
        if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
            l2 = (anIntArray1677[1] + anIntArray1677[2])
                    / (anIntArray1673[1] + anIntArray1673[2]);
        int k3 = 0;
        if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
            k3 = (anIntArray1677[3] + anIntArray1677[4])
                    / (anIntArray1673[3] + anIntArray1673[4]);
        int j4 = 0;
        if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
            j4 = (anIntArray1677[6] + anIntArray1677[8])
                    / (anIntArray1673[6] + anIntArray1673[8]);
        int i6 = 0;
        int k6 = anIntArray1673[10];
        int ai2[] = anIntArrayArray1674[10];
        int ai3[] = anIntArray1675;
        if (i6 == k6) {
            i6 = 0;
            k6 = anIntArray1673[11];
            ai2 = anIntArrayArray1674[11];
            ai3 = anIntArray1676;
        }
        int i5;
        if (i6 < k6)
            i5 = ai3[i6];
        else
            i5 = -1000;
        for (int l6 = 0; l6 < 10; l6++) {
            while (l6 == 0 && i5 > l2) {
                method484(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while (l6 == 3 && i5 > k3) {
                method484(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while (l6 == 5 && i5 > j4) {
                method484(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            int i7 = anIntArray1673[l6];
            int ai4[] = anIntArrayArray1674[l6];
            for (int j7 = 0; j7 < i7; j7++)
                method484(ai4[j7]);

        }

        while (i5 != -1000) {
            method484(ai2[i6++]);
            if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                i6 = 0;
                ai2 = anIntArrayArray1674[11];
                k6 = anIntArray1673[11];
                ai3 = anIntArray1676;
            }
            if (i6 < k6)
                i5 = ai3[i6];
            else
                i5 = -1000;
        }
    }

    private final void method484(int i) {
        if (outOfReach[i]) {
            method485(i);
            return;
        }
        int j = trianglesX[i];
        int k = trianglesY[i];
        int l = trianglesZ[i];
        Rasterizer.aBoolean1462 = hasAnEdgeToRestrict[i];
        if (alphas == null)
            Rasterizer.anInt1465 = 0;
        else
            Rasterizer.anInt1465 = alphas[i];
        int type;
        if (types == null)
            type = 0;
        else
            type = types[i] & 3;

        if(materials != null && materials[i] != -1) {
            int texture_a = j;
            int texture_b = k;
            int texture_c = l;
            if(textures != null && textures[i] != -1) {
                int coordinate = textures[i] & 0xff;
                texture_a = texturesX[coordinate];
                texture_b = texturesY[coordinate];
                texture_c = texturesZ[coordinate];
            }
            if(colorsZ[i] == -1 || type == 3) {
                Rasterizer.drawTexturedTriangle(
                        projected_verticesY[j], projected_verticesY[k], projected_verticesY[l],
                        projected_verticesX[j], projected_verticesX[k], projected_verticesX[l],
                        colorsX[i], colorsX[i], colorsX[i],
                        anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                        camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                        viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                        materials[i]);
            } else {
                Rasterizer.drawTexturedTriangle(
                        projected_verticesY[j], projected_verticesY[k], projected_verticesY[l],
                        projected_verticesX[j], projected_verticesX[k],projected_verticesX[l],
                        colorsX[i], colorsY[i], colorsZ[i],
                        anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                        camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                        viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                        materials[i]);
            }
        } else {
            if (type == 0) {
                Rasterizer.drawGouraudTriangle(projected_verticesY[j], projected_verticesY[k],
                        projected_verticesY[l], projected_verticesX[j], projected_verticesX[k],
                        projected_verticesX[l], colorsX[i], colorsY[i], colorsZ[i], camera_verticesZ[j],
                        camera_verticesZ[k], camera_verticesZ[l]);
                return;
            }
            if (type == 1) {
                Rasterizer.drawFlatTriangle(projected_verticesY[j], projected_verticesY[k],
                        projected_verticesY[l], projected_verticesX[j], projected_verticesX[k],
                        projected_verticesX[l], hsl2rgb[colorsX[i]], camera_verticesZ[j],
                        camera_verticesZ[k], camera_verticesZ[l]);
                ;
                return;
            }
        }
    }

    private final void method485(int i) {
        if (colors != null)
            if (colors[i] == 65535)
                return;
        int j = Rasterizer.textureInt1;
        int k = Rasterizer.textureInt2;
        int l = 0;
        int i1 = trianglesX[i];
        int j1 = trianglesY[i];
        int k1 = trianglesZ[i];
        int l1 = viewportTextureZ[i1];
        int i2 = viewportTextureZ[j1];
        int j2 = viewportTextureZ[k1];
        
        if (l1 >= 50) {
            anIntArray1678[l] = projected_verticesX[i1];
            anIntArray1679[l] = projected_verticesY[i1];
            anIntArray1680[l++] = colorsX[i];
        } else {
            int k2 = anIntArray1668[i1];
            int k3 = camera_verticesY[i1];
            int k4 = colorsX[i];
            if (j2 >= 50) {
                int k5 = (50 - l1) * lightDecay[j2 - l1];
                anIntArray1678[l] = j
                        + (k2 + ((anIntArray1668[k1] - k2) * k5 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1679[l] = k
                        + (k3 + ((camera_verticesY[k1] - k3) * k5 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1680[l++] = k4
                        + ((colorsZ[i] - k4) * k5 >> 16);
            }
            if (i2 >= 50) {
                int l5 = (50 - l1) * lightDecay[i2 - l1];
                anIntArray1678[l] = j
                        + (k2 + ((anIntArray1668[j1] - k2) * l5 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1679[l] = k
                        + (k3 + ((camera_verticesY[j1] - k3) * l5 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1680[l++] = k4
                        + ((colorsZ[i] - k4) * l5 >> 16);
            }
        }
        if (i2 >= 50) {
            anIntArray1678[l] = projected_verticesX[j1];
            anIntArray1679[l] = projected_verticesY[j1];
            anIntArray1680[l++] = colorsY[i];
        } else {
            int l2 = anIntArray1668[j1];
            int l3 = camera_verticesY[j1];
            int l4 = colorsY[i];
            if (l1 >= 50) {
                int i6 = (50 - i2) * lightDecay[l1 - i2];
                anIntArray1678[l] = j
                        + (l2 + ((anIntArray1668[i1] - l2) * i6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1679[l] = k
                        + (l3 + ((camera_verticesY[i1] - l3) * i6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1680[l++] = l4
                        + ((colorsX[i] - l4) * i6 >> 16);
            }
            if (j2 >= 50) {
                int j6 = (50 - i2) * lightDecay[j2 - i2];
                anIntArray1678[l] = j
                        + (l2 + ((anIntArray1668[k1] - l2) * j6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1679[l] = k
                        + (l3 + ((camera_verticesY[k1] - l3) * j6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1680[l++] = l4
                        + ((colorsX[i] - l4) * j6 >> 16);
            }
        }
        if (j2 >= 50) {
            anIntArray1678[l] = projected_verticesX[k1];
            anIntArray1679[l] = projected_verticesY[k1];
            anIntArray1680[l++] = colorsZ[i];
        } else {
            int i3 = anIntArray1668[k1];
            int i4 = camera_verticesY[k1];
            int i5 = colorsZ[i];
            if (i2 >= 50) {
                int k6 = (50 - j2) * lightDecay[i2 - j2];
                anIntArray1678[l] = j
                        + (i3 + ((anIntArray1668[j1] - i3) * k6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1679[l] = k
                        + (i4 + ((camera_verticesY[j1] - i4) * k6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1680[l++] = i5 + ((colorsY[i] - i5) * k6 >> 16);
            }
            if (l1 >= 50) {
                int l6 = (50 - j2) * lightDecay[l1 - j2];
                anIntArray1678[l] = j
                        + (i3 + ((anIntArray1668[i1] - i3) * l6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1679[l] = k
                        + (i4 + ((camera_verticesY[i1] - i4) * l6 >> 16) << WorldController.viewDistance)
                        / 50;
                anIntArray1680[l++] = i5 + ((colorsX[i] - i5) * l6 >> 16);
            }
        }
        int j3 = anIntArray1678[0];
        int j4 = anIntArray1678[1];
        int j5 = anIntArray1678[2];
        int i7 = anIntArray1679[0];
        int j7 = anIntArray1679[1];
        int k7 = anIntArray1679[2];
        if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
            Rasterizer.aBoolean1462 = false;
            int texture_a = i1;
            int texture_b = j1;
            int texture_c = k1;
            if (l == 3) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.centerX
                        || j4 > DrawingArea.centerX || j5 > DrawingArea.centerX)
                    Rasterizer.aBoolean1462 = true;
                int l7;
                if (types == null)
                    l7 = 0;
                else
                    l7 = types[i] & 3;
                if(materials != null && materials[i] != -1) {
                    if(textures != null && textures[i] != -1) {
                        int coordinate = textures[i] & 0xff;
                        texture_a = texturesX[coordinate];
                        texture_b = texturesY[coordinate];
                        texture_c = texturesZ[coordinate];
                    }
                    if(colorsZ[i] == -1) {
                        Rasterizer.drawTexturedTriangle(
                                i7, j7, k7,
                                j3, j4, j5,
                                colorsX[i], colorsX[i], colorsX[i],
                                anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                                camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                                viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                                materials[i]);
                    } else {
                        Rasterizer.drawTexturedTriangle(
                                i7, j7, k7,
                                j3, j4, j5,
                                anIntArray1680[0], anIntArray1680[1], anIntArray1680[2],
                                anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                                camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                                viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                                materials[i]);
                    }
                } else {
                    if (l7 == 0)
                        Rasterizer.drawGouraudTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], -1f, -1f, -1f);

                    else if (l7 == 1)
                        Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, hsl2rgb[colorsX[i]], -1f, -1f, -1f);
                }
            }
            if (l == 4) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.centerX
                        || j4 > DrawingArea.centerX || j5 > DrawingArea.centerX
                        || anIntArray1678[3] < 0
                        || anIntArray1678[3] > DrawingArea.centerX)
                    Rasterizer.aBoolean1462 = true;
                int i8;
                if (types == null)
                    i8 = 0;
                else
                    i8 = types[i] & 3;
                if(materials != null && materials[i] != -1) {
                    if(textures != null && textures[i] != -1) {
                        int coordinate = textures[i] & 0xff;
                        texture_a = texturesX[coordinate];
                        texture_b = texturesY[coordinate];
                        texture_c = texturesZ[coordinate];
                    }
                    if(colorsZ[i] == -1) {
                        Rasterizer.drawTexturedTriangle(
                                i7, j7, k7,
                                j3, j4, j5,
                                colorsX[i], colorsX[i], colorsX[i],
                                anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                                camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                                viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                                materials[i]);
                        Rasterizer.drawTexturedTriangle(
                                i7, k7, anIntArray1679[3],
                                j3, j5, anIntArray1678[3],
                                colorsX[i], colorsX[i], colorsX[i],
                                anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                                camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                                viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                                materials[i]);
                    } else {
                        Rasterizer.drawTexturedTriangle(
                                i7, j7, k7,
                                j3, j4, j5,
                                anIntArray1680[0], anIntArray1680[1], anIntArray1680[2],
                                anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                                camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                                viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                                materials[i]);
                        Rasterizer.drawTexturedTriangle(
                                i7, k7, anIntArray1679[3],
                                j3, j5, anIntArray1678[3],
                                anIntArray1680[0], anIntArray1680[2], anIntArray1680[3],
                                anIntArray1668[texture_a], anIntArray1668[texture_b], anIntArray1668[texture_c],
                                camera_verticesY[texture_a], camera_verticesY[texture_b], camera_verticesY[texture_c],
                                viewportTextureZ[texture_a], viewportTextureZ[texture_b], viewportTextureZ[texture_c],
                                materials[i]);
                        return;
                    }
                } else {
                    if (i8 == 0) {
                        Rasterizer.drawGouraudTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], -1f, -1f, -1f);
                        Rasterizer.drawGouraudTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1680[0], anIntArray1680[2], anIntArray1680[3], camera_verticesZ[i1], camera_verticesZ[j1], camera_verticesZ[k1]);
                        return;
                    }
                    if (i8 == 1) {
                        int l8 = hsl2rgb[colorsX[i]];
                        Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8, -1f, -1f, -1f);
                        Rasterizer.drawFlatTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], l8, camera_verticesZ[i1], camera_verticesZ[j1], camera_verticesZ[k1]);
                        return;
                    }
                }
            }
        }
    }

    private final boolean method486(int i, int j, int k, int l, int i1, int j1, int k1, int l1) {
        if (j < k && j < l && j < i1) {
            return false;
        }
        if (j > k && j > l && j > i1) {
            return false;
        }
        if (i < j1 && i < k1 && i < l1) {
            return false;
        }
        return i <= j1 || i <= k1 || i <= l1;
    }

    public void setTexture(int fromColor, int tex) {
        //printModelColours(this);
        int foundAmt = 0;
        int set2 = 0;
        for (int i = 0; i < colors.length; i++)
            if (fromColor == colors[i])
                foundAmt++;
        texturesCount = foundAmt;
        if (types == null)
            types = new int[foundAmt];
        if (colors == null)
            colors = new short[foundAmt];
        texturesX = new short[foundAmt];
        texturesY = new short[foundAmt];
        texturesZ = new short[foundAmt];
        int assigned = 0;
        for (int i = 0; i < trianglesCount; i++) {
            if (fromColor == colors[i]) {
                colors[i] = (short) tex;
                types[i] = 3 + set2;
                set2 += 4;
                texturesX[assigned] = (short) trianglesX[i];
                texturesY[assigned] = (short) trianglesY[i];
                texturesZ[assigned] = (short) trianglesZ[i];
                assigned++;
            }
        }
    }

    public void setTexture(int tex) {
        texturesCount = trianglesCount;
        int set2 = 0;
        if (types == null)
            types = new int[trianglesCount];
        if (colors == null)
            colors = new short[trianglesCount];
        texturesX = new short[trianglesCount];
        texturesY = new short[trianglesCount];
        texturesZ = new short[trianglesCount];

        for (int i = 0; i < trianglesCount; i++) {
            colors[i] = (short) tex;
            types[i] = 3 + set2;
            set2 += 4;
            texturesX[i] = (short) trianglesX[i];
            texturesY[i] = (short) trianglesY[i];
            texturesZ[i] = (short) trianglesZ[i];
        }
    }

    public static void printModelColours(Model model) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : model.colors) {
            list.add(i);
        }
        ArrayList<Integer> done = new ArrayList<Integer>();
        for (Integer i : list) {
            if (done.contains(i))
                continue;
            int amt = 0;
            for (Integer j : list) {
                if (j.intValue() == i.intValue())
                    amt++;
            }
            System.out.println(i + " on " + amt + " faces");
            done.add(i);
        }
    }
    public int[][] animayaGroups;
    public int[][] animayaScales;
    public short[] materials;
    public byte[] textures;
    public byte[] textureTypes;
    private boolean aBoolean1618;
    public static int anInt1620;
    public static Model EMPTY_MODEL = new Model(true);
    private static int anIntArray1622[] = new int[2000];
    private static int anIntArray1623[] = new int[2000];
    private static int anIntArray1624[] = new int[2000];
    private static int anIntArray1625[] = new int[2000];
    public int verticesCount;
    public int verticesX[];
    public int verticesY[];
    public int verticesZ[];
    public int trianglesCount;
    public int trianglesX[];
    public int trianglesY[];
    public int trianglesZ[];
    public int colorsX[];
    public int colorsY[];
    public int colorsZ[];
    public int types[];
    public byte[] face_render_priorities;
    public int alphas[];
    public short[] colors;
    public int face_priority;
    public int texturesCount;
    public short[] texturesX;
    public short[] texturesY;
    public short[] texturesZ;
    public int minimumXVertex;
    public int maximumXVertex;
    public int maximumZVertex;
    public int minimumZVertex;
    public int XYZMag;
    public int maximumYVertex;
    public int maxRenderDepth;
    public int diagonal3DAboveOrigin;
    public int itemDropHeight;
    public int vertexData[];
    public int triangleData[];
    public int vertexGroups[][];
    public int faceGroups[][];
    public boolean fits_on_single_square;
    VertexNormal vertexNormals[];
    public VertexNormal[] vertexNormalsOffsets;
    public FaceNormal[] faceNormals;
    static ModelHeader aClass21Array1661[];
    static OnDemandFetcherParent aOnDemandFetcherParent_1662;
    static boolean hasAnEdgeToRestrict[] = new boolean[15000];
    static boolean outOfReach[] = new boolean[15000];
    static int projected_verticesX[] = new int[15000];
    static int projected_verticesY[] = new int[15000];
    static int projected_verticesZ[] = new int[15000];
    static int anIntArray1668[] = new int[15000];
    static int camera_verticesY[] = new int[15000];
    static int viewportTextureZ[] = new int[15000];
    static int camera_verticesZ[] = new int[8000];
    static int depthListIndices[] = new int[15000];
    static int faceLists[][] = new int[15000][512];
    static int anIntArray1673[] = new int[12];
    static int anIntArrayArray1674[][] = new int[12][15000];
    static int anIntArray1675[] = new int[15000];
    static int anIntArray1676[] = new int[15000];
    static int anIntArray1677[] = new int[12];
    static int anIntArray1678[] = new int[10];
    static int anIntArray1679[] = new int[10];
    static int anIntArray1680[] = new int[10];
    static int xAnimOffset;
    static int yAnimOffset;
    static int zAnimOffset;
    public static boolean aBoolean1684;
    public static int anInt1685;
    public static int anInt1686;
    public static int obj_loaded;
    public static long obj_key[] = new long[15000];
    public static int SINE[];
    public static int COSINE[];
    static int hsl2rgb[];
    static int lightDecay[];

    static {
        SINE = Rasterizer.anIntArray1470;
        COSINE = Rasterizer.anIntArray1471;
        hsl2rgb = Rasterizer.hslToRgb;
        lightDecay = Rasterizer.anIntArray1469;
    }
    Model() {
        verticesCount = 0;
        trianglesCount = 0;
        texturesCount = 0;
        face_priority = 0;
        fits_on_single_square = true;
        this.xMidOffset = -1;
        this.yMidOffset = -1;
        this.zMidOffset = -1;
    }

    public void recolor(int found, int replace) {
        if(colors != null)
            for (int face = 0; face < trianglesCount; face++)
                if (colors[face] == (short) found)
                    colors[face] = (short) replace;
    }

    public void retexture(short found, short replace) {
        if(materials != null)
            for (int face = 0; face < trianglesCount; face++)
                if (materials[face] == found)
                    materials[face] = replace;
    }
    public int bufferOffset;
    public int uvBufferOffset;

    public java.util.List<net.runelite.api.model.Vertex> getVertices() {
        int[] verticesX = getVerticesX();
        int[] verticesY = getVerticesY();
        int[] verticesZ = getVerticesZ();
        ArrayList<net.runelite.api.model.Vertex> vertices = new ArrayList<>(getVerticesCount());
        for (int i = 0; i < getVerticesCount(); i++) {
            net.runelite.api.model.Vertex vertex = new net.runelite.api.model.Vertex(verticesX[i], verticesY[i], verticesZ[i]);
            vertices.add(vertex);
        }
        return vertices;
    }


    @Override
    public List<Triangle> getTriangles() {
        int[] trianglesX = getFaceIndices1();
        int[] trianglesY = getFaceIndices2();
        int[] trianglesZ = getFaceIndices3();

        List<Vertex> vertices = getVertices();
        List<Triangle> triangles = new ArrayList<>(getFaceCount());

        for (int i = 0; i < getFaceCount(); ++i)
        {
            int triangleX = trianglesX[i];
            int triangleY = trianglesY[i];
            int triangleZ = trianglesZ[i];

            Triangle triangle = new Triangle(vertices.get(triangleX),vertices.get(triangleY),vertices.get(triangleZ));
            triangles.add(triangle);
        }

        return triangles;
    }
    private int sceneId;
    @Override
    public int getSceneId() {
        return sceneId;
    }

    @Override
    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    @Override
    public int getBufferOffset() {
        return bufferOffset;
    }

    public void setBufferOffset(int bufferOffset) {
        this.bufferOffset = bufferOffset;
    }

    public int getUvBufferOffset() {
        return uvBufferOffset;
    }

    public void setUvBufferOffset(int uvBufferOffset) {
        this.uvBufferOffset = uvBufferOffset;
    }
    @Override
    public int getVerticesCount() {
        return verticesCount;
    }

    @Override
    public int[] getVerticesX() {
        return verticesX;
    }

    @Override
    public int[] getVerticesY() {
        return verticesY;
    }

    @Override
    public int[] getVerticesZ() {
        return verticesZ;
    }

    @Override
    public int getFaceCount() {
        return trianglesCount;
    }

    @Override
    public int[] getFaceIndices1() {
        return trianglesX;
    }

    @Override
    public int[] getFaceIndices2() {
        return trianglesY;
    }

    @Override
    public int[] getFaceIndices3() {
        return trianglesZ;
    }

    @Override
    public int[] getFaceColors1() {
        return colorsX;
    }

    @Override
    public int[] getFaceColors2() {
        return colorsY;
    }

    @Override
    public int[] getFaceColors3() {
        return colorsZ;
    }

    @Override
    public int[] getFaceTransparencies() {
        int[] alphas = this.alphas;
        return alphas;
    }

    @Override
    public byte[] getFaceRenderPriorities() {
        return face_render_priorities;
    }

    @Override
    public int[][] getVertexGroups() {
        return new int[0][];
    }

    @Override
    public void animate(int type, int[] list, int x, int y, int z) {

    }

    @Override
    public void calculateBoundsCylinder() {
        calculateDistances();
    }

    @Override
    public void calculateExtreme(int orientation) {

    }

    @Override
    public void resetBounds() {

    }

    @Override
    public RSModel toSharedModel(boolean b) {
        return null;
    }

    @Override
    public RSModel toSharedSpotAnimModel(boolean b) {
        return null;
    }

    public void rotateY90Ccw() {
        for (int var1 = 0; var1 < this.getVerticesCount(); ++var1)
        {
            int var2 = this.getVerticesX()[var1];
            this.getVerticesX()[var1] = this.getVerticesZ()[var1];
            this.getVerticesZ()[var1] = -var2;
        }

    }

    @Override
    public void rotateY180Ccw() {
        for (int var1 = 0; var1 < this.getVerticesCount(); ++var1)
        {
            this.getVerticesX()[var1] = -this.getVerticesX()[var1];
            this.getVerticesZ()[var1] = -this.getVerticesZ()[var1];
        }

    }

    @Override
    public void rotateY270Ccw() {
        for (int var1 = 0; var1 < this.getVerticesCount(); ++var1)
        {
            int var2 = this.getVerticesZ()[var1];
            this.getVerticesZ()[var1] = this.getVerticesX()[var1];
            this.getVerticesX()[var1] = -var2;
        }
    }
    @Override
    public int getRadius() {
        return diagonal3DAboveOrigin;
    }

    @Override
    public int getCenterX() {
        return xMid;
    }

    @Override
    public int getCenterY() {
        return yMid;
    }

    @Override
    public int getCenterZ() {
        return zMid;
    }

    @Override
    public int getExtremeX() {
        return xMidOffset;
    }

    @Override
    public int getExtremeY() {
        return yMidOffset;
    }

    @Override
    public int getExtremeZ() {
        return zMidOffset;
    }

    @Override
    public short[] getFaceTextures() {
        return materials;
    }

    @Override
    public int getXYZMag() {
        return XYZMag;
    }

    @Override
    public boolean isClickable() {
        return fits_on_single_square;
    }

    @Override
    public int getBottomY() {
        return modelHeight;
    }

    @Override
    public void drawFace(int face) {

    }

    @Override
    public void interpolateFrames(RSFrames frames, int frameId, RSFrames nextFrames, int nextFrameId, int interval, int intervalCount) {

    }
    private float[] faceTextureUVCoordinates;
    private int[] vertexNormalsX, vertexNormalsY, vertexNormalsZ;
    @Override
    public int[] getVertexNormalsX() {
        if(vertexNormalsX == null)
            return getVerticesX();
        return vertexNormalsX;
    }

    @Override
    public void setVertexNormalsX(int[] vertexNormalsX) {
        this.vertexNormalsX = vertexNormalsX;
    }

    @Override
    public int[] getVertexNormalsY() {
        if(vertexNormalsY == null)
            return getVerticesY();
        return vertexNormalsY;
    }

    @Override
    public void setVertexNormalsY(int[] vertexNormalsY) {
        this.vertexNormalsY = vertexNormalsY;
    }

    @Override
    public int[] getVertexNormalsZ() {
        if(vertexNormalsZ == null)
            return getVerticesZ();
        return vertexNormalsZ;
    }

    @Override
    public void setVertexNormalsZ(int[] vertexNormalsZ) {
        this.vertexNormalsZ = vertexNormalsZ;
    }

    @Override
    public byte getOverrideAmount() {
        return 0;
    }

    @Override
    public byte getOverrideHue() {
        return 0;
    }

    @Override
    public byte getOverrideSaturation() {
        return 0;
    }

    @Override
    public byte getOverrideLuminance() {
        return 0;
    }

    @Override
    public Shape getConvexHull(int localX, int localY, int orientation, int tileHeight) {
        int[] x2d = new int[getVerticesCount()];
        int[] y2d = new int[getVerticesCount()];

        Perspective.modelToCanvas(Client.instance, getVerticesCount(), localX, localY, tileHeight, orientation, getVerticesX(), getVerticesZ(), getVerticesY(), x2d, y2d);

        return Jarvis.convexHull(x2d, y2d);
    }


    @Override
    public float[] getFaceTextureUVCoordinates() {
        computeTextureUvCoordinates();
        return faceTextureUVCoordinates;
    }

    @Override
    public void setFaceTextureUVCoordinates(float[] faceTextureUVCoordinates) {
        this.faceTextureUVCoordinates = faceTextureUVCoordinates;
    }


    private void vertexNormals()
    {

        if (vertexNormalsX == null)
        {
            int verticesCount = getVerticesCount();

            vertexNormalsX = new int[verticesCount];
            vertexNormalsY = new int[verticesCount];
            vertexNormalsZ = new int[verticesCount];

            int[] trianglesX = getFaceIndices1();
            int[] trianglesY = getFaceIndices2();
            int[] trianglesZ = getFaceIndices3();
            int[] verticesX = getVerticesX();
            int[] verticesY = getVerticesY();
            int[] verticesZ = getVerticesZ();

            for (int i = 0; i < trianglesCount; ++i)
            {
                int var9 = trianglesX[i];
                int var10 = trianglesY[i];
                int var11 = trianglesZ[i];

                int var12 = verticesX[var10] - verticesX[var9];
                int var13 = verticesY[var10] - verticesY[var9];
                int var14 = verticesZ[var10] - verticesZ[var9];
                int var15 = verticesX[var11] - verticesX[var9];
                int var16 = verticesY[var11] - verticesY[var9];
                int var17 = verticesZ[var11] - verticesZ[var9];

                int var18 = var13 * var17 - var16 * var14;
                int var19 = var14 * var15 - var17 * var12;

                int var20;
                for (var20 = var12 * var16 - var15 * var13; var18 > 8192 || var19 > 8192 || var20 > 8192 || var18 < -8192 || var19 < -8192 || var20 < -8192; var20 >>= 1)
                {
                    var18 >>= 1;
                    var19 >>= 1;
                }

                int var21 = (int) Math.sqrt(var18 * var18 + var19 * var19 + var20 * var20);
                if (var21 <= 0)
                {
                    var21 = 1;
                }

                var18 = var18 * 256 / var21;
                var19 = var19 * 256 / var21;
                var20 = var20 * 256 / var21;

                vertexNormalsX[var9] += var18;
                vertexNormalsY[var9] += var19;
                vertexNormalsZ[var9] += var20;

                vertexNormalsX[var10] += var18;
                vertexNormalsY[var10] += var19;
                vertexNormalsZ[var10] += var20;

                vertexNormalsX[var11] += var18;
                vertexNormalsY[var11] += var19;
                vertexNormalsZ[var11] += var20;
            }
        }
    }

    public void computeTextureUvCoordinates()
    {
        final short[] faceTextures = getFaceTextures();
        if (faceTextures == null)
        {
            return;
        }

        final int[] vertexPositionsX = getVertexNormalsX();
        final int[] vertexPositionsY = getVertexNormalsY();
        final int[] vertexPositionsZ = getVertexNormalsZ();

        final int[] trianglePointsX = getFaceIndices1();
        final int[] trianglePointsY = getFaceIndices2();
        final int[] trianglePointsZ = getFaceIndices3();

        final short[] texTriangleX = texturesX;
        final short[] texTriangleY = texturesY;
        final short[] texTriangleZ = texturesZ;

        final byte[] textureCoords = textures;

        int faceCount = trianglesCount;
        float[] faceTextureUCoordinates = new float[faceCount * 6];

        for (int i = 0; i < faceCount; i++)
        {
            int trianglePointX = trianglePointsX[i];
            int trianglePointY = trianglePointsY[i];
            int trianglePointZ = trianglePointsZ[i];

            short textureIdx = faceTextures[i];

            if (textureIdx != -1)
            {
                int triangleVertexIdx1;
                int triangleVertexIdx2;
                int triangleVertexIdx3;

                if (textureCoords != null && textureCoords[i] != -1)
                {
                    int textureCoordinate = textureCoords[i] & 255;
                    triangleVertexIdx1 = texTriangleX[textureCoordinate];
                    triangleVertexIdx2 = texTriangleY[textureCoordinate];
                    triangleVertexIdx3 = texTriangleZ[textureCoordinate];
                }
                else
                {
                    triangleVertexIdx1 = trianglePointX;
                    triangleVertexIdx2 = trianglePointY;
                    triangleVertexIdx3 = trianglePointZ;
                }

                float triangleX = (float) vertexPositionsX[triangleVertexIdx1];
                float triangleY = (float) vertexPositionsY[triangleVertexIdx1];
                float triangleZ = (float) vertexPositionsZ[triangleVertexIdx1];

                float f_882_ = (float) vertexPositionsX[triangleVertexIdx2] - triangleX;
                float f_883_ = (float) vertexPositionsY[triangleVertexIdx2] - triangleY;
                float f_884_ = (float) vertexPositionsZ[triangleVertexIdx2] - triangleZ;
                float f_885_ = (float) vertexPositionsX[triangleVertexIdx3] - triangleX;
                float f_886_ = (float) vertexPositionsY[triangleVertexIdx3] - triangleY;
                float f_887_ = (float) vertexPositionsZ[triangleVertexIdx3] - triangleZ;
                float f_888_ = (float) vertexPositionsX[trianglePointX] - triangleX;
                float f_889_ = (float) vertexPositionsY[trianglePointX] - triangleY;
                float f_890_ = (float) vertexPositionsZ[trianglePointX] - triangleZ;
                float f_891_ = (float) vertexPositionsX[trianglePointY] - triangleX;
                float f_892_ = (float) vertexPositionsY[trianglePointY] - triangleY;
                float f_893_ = (float) vertexPositionsZ[trianglePointY] - triangleZ;
                float f_894_ = (float) vertexPositionsX[trianglePointZ] - triangleX;
                float f_895_ = (float) vertexPositionsY[trianglePointZ] - triangleY;
                float f_896_ = (float) vertexPositionsZ[trianglePointZ] - triangleZ;

                float f_897_ = f_883_ * f_887_ - f_884_ * f_886_;
                float f_898_ = f_884_ * f_885_ - f_882_ * f_887_;
                float f_899_ = f_882_ * f_886_ - f_883_ * f_885_;
                float f_900_ = f_886_ * f_899_ - f_887_ * f_898_;
                float f_901_ = f_887_ * f_897_ - f_885_ * f_899_;
                float f_902_ = f_885_ * f_898_ - f_886_ * f_897_;
                float f_903_ = 1.0F / (f_900_ * f_882_ + f_901_ * f_883_ + f_902_ * f_884_);

                float u0 = (f_900_ * f_888_ + f_901_ * f_889_ + f_902_ * f_890_) * f_903_;
                float u1 = (f_900_ * f_891_ + f_901_ * f_892_ + f_902_ * f_893_) * f_903_;
                float u2 = (f_900_ * f_894_ + f_901_ * f_895_ + f_902_ * f_896_) * f_903_;

                f_900_ = f_883_ * f_899_ - f_884_ * f_898_;
                f_901_ = f_884_ * f_897_ - f_882_ * f_899_;
                f_902_ = f_882_ * f_898_ - f_883_ * f_897_;
                f_903_ = 1.0F / (f_900_ * f_885_ + f_901_ * f_886_ + f_902_ * f_887_);

                float v0 = (f_900_ * f_888_ + f_901_ * f_889_ + f_902_ * f_890_) * f_903_;
                float v1 = (f_900_ * f_891_ + f_901_ * f_892_ + f_902_ * f_893_) * f_903_;
                float v2 = (f_900_ * f_894_ + f_901_ * f_895_ + f_902_ * f_896_) * f_903_;

                int idx = i * 6;
                faceTextureUCoordinates[idx] = u0;
                faceTextureUCoordinates[idx + 1] = v0;
                faceTextureUCoordinates[idx + 2] = u1;
                faceTextureUCoordinates[idx + 3] = v1;
                faceTextureUCoordinates[idx + 4] = u2;
                faceTextureUCoordinates[idx + 5] = v2;
            }
        }

        faceTextureUVCoordinates = faceTextureUCoordinates;
    }
}