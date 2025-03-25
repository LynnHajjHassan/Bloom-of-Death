package BOD;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class Carpet {
    private static BODObjects[] carpetParts = new BODObjects[1];

    protected static TransformGroup create_Carpet() {
        TransformGroup carpetTG = new TransformGroup();
        carpetParts[0] = new CarpetMain();
        carpetTG = carpetParts[0].position_Object();
        return carpetTG;
    }
}

class CarpetMain extends BODObjects {
    public CarpetMain() {
        scale = 1.0d; // scale is already handled by geometry size
        post = new Vector3f(0.0f, -0.445f, 0.25f);
        create_Geometry();
        create_Appearance();
    }

    private void create_Geometry() {
        float halfX = 0.8f;
        float halfY = 0.01f;   // thickness
        float halfZ = 0.8f;

        Point3f[] vertices = {
            // Top face
            new Point3f(-halfX, halfY, -halfZ),
            new Point3f(halfX, halfY, -halfZ),
            new Point3f(halfX, halfY, halfZ),
            new Point3f(-halfX, halfY, halfZ),

            // Bottom face
            new Point3f(-halfX, -halfY, -halfZ),
            new Point3f(halfX, -halfY, -halfZ),
            new Point3f(halfX, -halfY, halfZ),
            new Point3f(-halfX, -halfY, halfZ),

            // Front face
            new Point3f(-halfX, -halfY, -halfZ),
            new Point3f(halfX, -halfY, -halfZ),
            new Point3f(halfX, halfY, -halfZ),
            new Point3f(-halfX, halfY, -halfZ),

            // Back face
            new Point3f(-halfX, -halfY, halfZ),
            new Point3f(halfX, -halfY, halfZ),
            new Point3f(halfX, halfY, halfZ),
            new Point3f(-halfX, halfY, halfZ),

            // Left face
            new Point3f(-halfX, -halfY, -halfZ),
            new Point3f(-halfX, -halfY, halfZ),
            new Point3f(-halfX, halfY, halfZ),
            new Point3f(-halfX, halfY, -halfZ),

            // Right face
            new Point3f(halfX, -halfY, -halfZ),
            new Point3f(halfX, -halfY, halfZ),
            new Point3f(halfX, halfY, halfZ),
            new Point3f(halfX, halfY, -halfZ)
        };

        QuadArray geometry = new QuadArray(24, GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);
        geometry.setCoordinates(0, vertices);

        // Set normals per face
        assignNormal(geometry, 0, new Vector3f(0, 1, 0));    // top
        assignNormal(geometry, 4, new Vector3f(0, -1, 0));   // bottom
        assignNormal(geometry, 8, new Vector3f(0, 0, -1));   // front
        assignNormal(geometry, 12, new Vector3f(0, 0, 1));   // back
        assignNormal(geometry, 16, new Vector3f(-1, 0, 0));  // left
        assignNormal(geometry, 20, new Vector3f(1, 0, 0));   // right

        // Texture coordinates for top face only
        geometry.setTextureCoordinate(0, 0, new TexCoord2f(0, 0));
        geometry.setTextureCoordinate(0, 1, new TexCoord2f(1, 0));
        geometry.setTextureCoordinate(0, 2, new TexCoord2f(1, 1));
        geometry.setTextureCoordinate(0, 3, new TexCoord2f(0, 1));

        for (int i = 4; i < 24; i++) {
            geometry.setTextureCoordinate(0, i, new TexCoord2f(0, 0));
        }

        obj_shape = new Shape3D(geometry);
        objBG.addChild(obj_shape);
    }

    protected void create_Appearance() {
        mtl_clr[0] = new Color3f(0.8f, 0.8f, 0.8f);
        mtl_clr[1] = new Color3f(0.0f, 0.0f, 0.0f);
        mtl_clr[2] = new Color3f(0.3f, 0.3f, 0.3f);
        mtl_clr[3] = new Color3f(0.1f, 0.1f, 0.1f);
        obj_Appearance();

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);
        app.setTexture(textured_App("carpet"));

        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(polyAttr);

        obj_shape.setAppearance(app);
    }

    private void assignNormal(QuadArray geometry, int startIndex, Vector3f normal) {
        for (int i = 0; i < 4; i++) {
            geometry.setNormal(startIndex + i, normal);
        }
    }

    public TransformGroup position_Object() {
        objTG.addChild(objBG);
        return objTG;
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG);
    }
}
