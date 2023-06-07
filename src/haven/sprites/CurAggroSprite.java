package haven.sprites;

import haven.*;
import haven.sprites.mesh.ObstMesh;
import haven.render.BaseColor;
import haven.render.RenderTree;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class CurAggroSprite extends Sprite {
    public static final int id = -59129521;
    private static final ObstMesh mesh;
    private static final BaseColor col = new BaseColor(Color.RED);

    static {
	final Coord2d[][] shapes = new Coord2d[4][4];
	final Coord2d offset = new Coord2d(10, 0);
	{
	    shapes[0][0] = offset.rotate(Math.toRadians(35));
	    shapes[0][1] = offset.rotate(Math.toRadians(180 + 55));
	    shapes[0][2] = offset.rotate(Math.toRadians(180 + 35));
	    shapes[0][3] = offset.rotate(Math.toRadians(55));

	    shapes[1][0] = offset.rotate(Math.toRadians(125));
	    shapes[1][1] = offset.rotate(Math.toRadians(180 + 145));
	    shapes[1][2] = offset.rotate(Math.toRadians(180 + 125));
	    shapes[1][3] = offset.rotate(Math.toRadians(145));

	    shapes[2][0] = offset.rotate(Math.toRadians(-10));
	    shapes[2][1] = offset.rotate(Math.toRadians(180 + 10));
	    shapes[2][2] = offset.rotate(Math.toRadians(180 - 10));
	    shapes[2][3] = offset.rotate(Math.toRadians(10));

	    shapes[3][0] = offset.rotate(Math.toRadians(80));
	    shapes[3][1] = offset.rotate(Math.toRadians(180 + 100));
	    shapes[3][2] = offset.rotate(Math.toRadians(180 + 80));
	    shapes[3][3] = offset.rotate(Math.toRadians(100));
	}
	mesh = makeMesh(shapes, col.color(), 0.5F);
    }

    public CurAggroSprite(final Gob g) {
	super(g, null);
    }

    @Override
    public void added(RenderTree.Slot slot) {
	super.added(slot);
	slot.add(mesh, col);
    }

    @Override
    public String toString() {
	return "CurAggroSprite";
    }

	public static ObstMesh makeMesh(final Coord2d[][] shapes, final Color col, final float h) {
		final int polygons = shapes.length;
		final float[] hiddencolor = Utils.c2fa(col);
		final FloatBuffer pa, na, cl;
		final ShortBuffer sa;

		{
			int verts = 0, inds = 0;
			for (Coord2d[] shape : shapes) {
				verts += shape.length;
				inds += (int) (Math.ceil(shape.length / 3.0));
			}
			pa = Utils.mkfbuf(verts * 3);
			na = Utils.mkfbuf(verts * 3);
			cl = Utils.mkfbuf(verts * 4);
			sa = Utils.mksbuf(inds * 3);
		}

		for (Coord2d[] shape : shapes) {
			for (final Coord2d off : shape) {
				pa.put((float) off.x).put((float) off.y).put(h);
				na.put((float) off.x).put((float) off.y).put(0f);
				cl.put(hiddencolor[0]).put(hiddencolor[1]).put(hiddencolor[2]).put(hiddencolor[3]);
			}
		}

		short voff = 0;
		for (int poly = 0; poly < polygons; ++poly) {
			final int vertsper = shapes[poly].length;
			for (int j = 0; j < (int) Math.ceil(vertsper / 3.0); ++j) {
				short s1 = (short) ((voff * j % vertsper) + (poly * vertsper));
				short s2 = (short) (((voff * j + 1) % vertsper) + (poly * vertsper));
				short s3 = (short) (((voff * j + 2) % vertsper) + (poly * vertsper));
				sa.put(s1).put(s2).put(s3);
				voff += 2;
			}
			voff = 0;
		}

		return new ObstMesh(new VertexBuf(new VertexBuf.VertexData(pa),
				new VertexBuf.NormalData(na),
				new VertexBuf.ColorData(cl)),
				sa);
	}
}
