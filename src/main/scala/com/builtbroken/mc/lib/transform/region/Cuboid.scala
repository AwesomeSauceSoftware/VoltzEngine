package com.builtbroken.mc.lib.transform.region

import java.math.{BigDecimal, MathContext, RoundingMode}
import java.util.{ArrayList, List}

import com.builtbroken.jlib.data.vector.IPos3D
import com.builtbroken.mc.lib.transform.vector.{Point, Pos}
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.util.{AxisAlignedBB, Vec3}
import net.minecraft.world.World
class Cuboid(var min: IPos3D, var max: IPos3D)
{
  def this() = this(new Pos(), new Pos())

  def this(amount: Double) = this(new Pos(), new Pos(amount))

  def this(cuboid: Cuboid) = this(cuboid.min, cuboid.max)

  def this(minx: Double, miny: Double, minz: Double, maxx: Double, maxy: Double, maxz: Double) = this(new Pos(minx, miny, minz), new Pos(maxx, maxy, maxz))

  def this(aabb: AxisAlignedBB) = this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ)

  def this(block: Block) = this(block.getBlockBoundsMinX, block.getBlockBoundsMinY, block.getBlockBoundsMinZ, block.getBlockBoundsMaxX, block.getBlockBoundsMaxY, block.getBlockBoundsMaxZ)

  def toAABB: AxisAlignedBB = AxisAlignedBB.getBoundingBox(min.x, min.y, min.z, max.x, max.y, max.z)

  def toRectangle: Rectangle = new Rectangle(new Point(min), new Point(max))

  def set(other: Cuboid): Cuboid =
  {
    min = other.min
    max = other.max
    return this
  }

  /**
   * Conversion
   */

  def add(vec: IPos3D): Cuboid =
  {
    min = new Pos(min).add(vec)
    max = new Pos(max).add(vec)
    return this;
  }

  def add(x: Double, y: Double, z: Double): Cuboid = this.add(new Pos(x, y, z))

  def subtract(vec: IPos3D): Cuboid =
  {
    min = new Pos(min).sub(vec)
    max = new Pos(max).sub(vec)
    return this;
  }

  def setBounds(block: Block): Cuboid =
  {
    block.setBlockBounds(min.x.asInstanceOf[Float], min.y.asInstanceOf[Float], min.z.asInstanceOf[Float], max.x.asInstanceOf[Float], max.y.asInstanceOf[Float], max.z.asInstanceOf[Float])
    return this
  }

  def intersects(v: Vec3): Boolean =
  {
    return intersects(v.xCoord, v.yCoord, v.zCoord)
  }
  
  def intersects(v: IPos3D): Boolean =
  {
    return intersects(v.x, v.y, v.z)
  }

  def intersects(x: Double, y: Double, z: Double) : Boolean =
  {
    return isWithinX(x) && isWithinY(y) && isWithinZ(z)
  }

  def doesOverlap(box: AxisAlignedBB): Boolean =
  {
    return doesOverlap(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
  }

  def doesOverlap(box: Cuboid): Boolean =
  {
    return doesOverlap(box.min.x, box.min.y, box.min.z, box.max.x, box.max.y, box.max.z)
  }

  def doesOverlap(x: Double, y: Double, z: Double, i: Double, j: Double, k: Double): Boolean =
  {
    return !isOutSideX(x, i) || !isOutSideY(y, j) || !isOutSideZ(z, k)
  }

  def isOutSideX(x: Double, i: Double): Boolean = (min.x > x || i > max.x)
  def isOutSideY(y: Double, j: Double): Boolean = (min.y > y || j > max.y)
  def isOutSideZ(z: Double, k: Double): Boolean = (min.z > z || k > max.z)

  def isInsideBounds(x: Double, y: Double, z: Double, i: Double, j: Double, k: Double): Boolean =
  {
    return isWithin(min.x, max.x, x, i) && isWithin(min.y, max.y, y, j) && isWithin(min.z, max.z, z, k)
  }

  def isInsideBounds(other: Cuboid): Boolean =
  {
    return isInsideBounds(other.min.x, other.min.y, other.min.z, other.max.x, other.max.y, other.max.z)
  }

  def isInsideBounds(other: AxisAlignedBB) : Boolean =
  {
    return isInsideBounds(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ)
  }
  
  def isVecInYZ(v : Vec3): Boolean = isWithinY(v) && isWithinZ(v)
  def isVecInYZ(v : IPos3D): Boolean = isWithinY(v) && isWithinZ(v)

  def isWithinXZ(v : Vec3): Boolean = isWithinX(v) && isWithinZ(v)
  def isWithinXZ(v : IPos3D): Boolean = isWithinX(v) && isWithinZ(v)
  
  def isWithinX(v: Double): Boolean = isWithin(min.x, max.x, v)
  def isWithinX(v: Vec3): Boolean = isWithinX(v.xCoord)
  def isWithinX(v: IPos3D): Boolean = isWithinX(v.x())

  def isWithinY(v: Double): Boolean = isWithin(min.y, max.y, v)
  def isWithinY(v: Vec3): Boolean = isWithinY(v.yCoord)
  def isWithinY(v: IPos3D): Boolean = isWithinY(v.y())

  def isWithinZ(v: Double): Boolean = isWithin(min.z, max.z, v)
  def isWithinZ(v: Vec3): Boolean = isWithinZ(v.zCoord)
  def isWithinZ(v: IPos3D): Boolean = isWithinZ(v.z())
  
  def isWithin(min: Double, max: Double, v: Double) : Boolean = v >= min + 1E-5  && v <= max - 1E-5

  /** Checks to see if a line segment is within the defined line. Assume the lines overlap each other.
   * @param min - min point
   * @param max - max point
   * @param a - min line point
   * @param b - max line point
   * @return true if the line segment is within the bounds
   */
  def isWithin(min: Double, max: Double, a: Double , b: Double) : Boolean = a + 1E-5 >= min  && b - 1E-5 <= max

  def center: Pos = new Pos((max.x() - min.x()) / 2, (max.y() - min.y()) / 2, (max.z() - min.z()) / 2)

  def expand(d: IPos3D): Cuboid =
  {
    min = new Pos(min.x() - d.x(), min.y() - d.y(), min.z() - d.z())
    max = new Pos(max.x() + d.x(), max.y() + d.y(), max.z() + d.z())
    return this
  }

  def expand(d: Double): Cuboid =
  {
    min = new Pos(min.x() - d, min.y() - d, min.z() - d)
    max = new Pos(max.x() + d, max.y() + d, max.z() + d)
    return this
  }

  /**
   * Iterates through block positions in this cubic region.
   * @param callback - The method we want to call back
   */
  def foreach(callback: Pos => Unit)
  {
    for (x <- min.x().asInstanceOf[Int] until max.x().asInstanceOf[Int])
    {
      for (y <- min.y().asInstanceOf[Int] until max.y().asInstanceOf[Int])
      {
        for (z <- min.z().asInstanceOf[Int] until max.z().asInstanceOf[Int])
        {
          callback(new Pos(x, y, z).floor);
        }
      }
    }
  }

  /** @return List of vector block positions within this region. */
  def getVectors: List[Pos] =
  {
    val vectors = new ArrayList[Pos];
    foreach(vector => vectors.add(vector))
    return vectors
  }

  def getVectors(center: Pos, radius: Int): List[Pos] =
  {
    val vectors = new ArrayList[Pos];
    foreach(vector => if (center.distance(vector.asInstanceOf[IPos3D]) <= radius) vectors.add(vector))
    return vectors
  }

  def radius : Double =
  {
    var m: Double = 0;
    if(xSize > m)
      m = xSize
    if(ySize > m)
      m = ySize
    if(zSize > m)
      m = zSize
    return m
  }

  def isSquared: Boolean = xSize == ySize && ySize == zSize
  def isSquaredInt: Boolean = xSizeInt == ySizeInt && ySizeInt == zSizeInt
  
  def xSize: Double = max.x - min.x
  def ySize: Double = max.y - min.y
  def zSize: Double = max.z - min.z

  def xSizeInt: Int = (max.x - min.x).asInstanceOf[Int]
  def ySizeInt: Int = (max.y - min.y).asInstanceOf[Int]
  def zSizeInt: Int = (max.z - min.z).asInstanceOf[Int]

  def distance(v: Vec3): Double = center.distance(v)
  def distance(v: IPos3D): Double = center.distance(v)
  def distance(box: Cuboid): Double = distance(box.center)
  def distance(box: AxisAlignedBB): Double = distance(new Cuboid(box))
  def distance(xx: Double, yy: Double, zz: Double) : Double =
  {
    val center = this.center
    val x = center.x - xx;
    val y = center.y - yy;
    val z = center.z - zz;
    return Math.sqrt(x * x + y * y + z * z)
  }

  def volume() : Double =
  {
    return xSize *  ySize * zSize
  }

  def area() : Double =
  {
    return (2 * xSize * zSize) + (2 * xSize * ySize) + (2 * zSize * ySize)
  }

  def getCorners(box: Cuboid): Array[IPos3D] =
  {
    val array: Array[IPos3D] = new Array[IPos3D](8)
    val l: Double = box.max.x - box.min.x
    val w: Double = box.max.z - box.min.z
    val h: Double = box.max.y - box.min.y
    array(0) = new Pos(box.min.x, box.min.y, box.min.z)
    array(1) = new Pos(box.min.x, box.min.y + h, box.min.z)
    array(2) = new Pos(box.min.x, box.min.y + h, box.min.z + w)
    array(3) = new Pos(box.min.x, box.min.y, box.min.z + w)
    array(4) = new Pos(box.min.x + l, box.min.y, box.min.z)
    array(5) = new Pos(box.min.x + l, box.min.y + h, box.min.z)
    array(6) = new Pos(box.min.x + l, box.min.y + h, box.min.z + w)
    array(7) = new Pos(box.min.x + l, box.min.y, box.min.z + w)
    return array
  }

  def getCorners(box: AxisAlignedBB): Array[IPos3D] =
  {
    val array: Array[IPos3D] = new Array[IPos3D](8)
    val l: Double = box.maxX - box.minX
    val w: Double = box.maxZ - box.minZ
    val h: Double = box.maxY - box.minY
    array(0) = new Pos(box.minX, box.minY, box.minZ)
    array(1) = new Pos(box.minX, box.minY + h, box.minZ)
    array(2) = new Pos(box.minX, box.minY + h, box.minZ + w)
    array(3) = new Pos(box.minX, box.minY, box.minZ + w)
    array(4) = new Pos(box.minX + l, box.minY, box.minZ)
    array(5) = new Pos(box.minX + l, box.minY + h, box.minZ)
    array(6) = new Pos(box.minX + l, box.minY + h, box.minZ + w)
    array(7) = new Pos(box.minX + l, box.minY, box.minZ + w)
    return array
  }

  /** Returns all entities in this region. */
  def getEntities(world: World): List[Entity] = getEntities(world, classOf[Entity])

  def getEntities[E <: Entity](world: World, entityClass: Class[E]): List[E] = world.getEntitiesWithinAABB(entityClass, toAABB).asInstanceOf[List[E]]

  def getEntitiesExclude(world: World, entity: Entity): List[Entity] = world.getEntitiesWithinAABBExcludingEntity(entity, toAABB).asInstanceOf[List[Entity]]

  override def toString: String =
  {
    val cont: MathContext = new MathContext(4, RoundingMode.HALF_UP)
    return "Cuboid[" + new BigDecimal(min.x, cont) + ", " + new BigDecimal(min.y, cont) + ", " + new BigDecimal(min.z, cont) + "] -> [" + new BigDecimal(max.x, cont) + ", " + new BigDecimal(max.y, cont) + ", " + new BigDecimal(max.z, cont) + "]"
  }

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[Cuboid]) return (min == (o.asInstanceOf[Cuboid]).min) && (max == (o.asInstanceOf[Cuboid]).max)
    return false
  }

  override def clone: Cuboid = new Cuboid(this)
}