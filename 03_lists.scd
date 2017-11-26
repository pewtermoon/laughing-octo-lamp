// Practice auto-populating lists
s.boot;

(
var y, z;
z = List[1, 2, 3, 4, 5];
y = z.copyRange(1,3);
z.postln;
y.postln;
)



List['a', 'b', 'c'].do({ arg item, i; [i, item].postln; });
(
'arrays------------------------------'.postln;
//x = Array.new(3);
y = List.new(3);
y.do()
//5.do({ arg i; z = x.add(i); y.add(i); });
//x.postln;
//z.postln;
y.postln;
)

s.quit;

// Make a sequential list
List.fill(10, { arg i; i+1 });

// More complex
List.fill(8, { arg i; (i+1)/4 });

// What about 4 ones?
List.fill(4, { arg i; (i+1)/(i+1) });

//
List.fill(4, { arg i; (i+1)/(i+1) });

Array.fill([2, 2, 3], { arg i, j, k;  i * 100 + (j * 10) + k });

// Nested
List.fill([2,1], { arg i; i});

// SERIES!!!!
Array.series(5, 10, 2).postln;
List.series(5, 10, 2).postln;

Array.series(8,0,0.25).postln;

Array.series(size, start: 0, step: 1)

// Exercise. How to get e.g.
//s1 = [2, 1, 0.5, 0.5, 0.25, 0.25, 0.25, 0.25, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125];
//s3 = [0.5, 0.6, 0.7, 0.8, 0.9, 1, 1.1, 1.2, 1.3].reverse;
//s2 = [100000, 150000, 200000, 250000, 100000, 100000, 230000, 500000, 110000];
Array.series(9, 0.5, 0.1)
Array.series(9, 100000,50000)

//
// How do we get this? [2, 1, 1, 0.5, 0.5, 0.5, 0.5]
Array.fill(10, Pxrand([0, 1, 2], inf).iter);

Array.fill(10, Pseq([1,2],10).iter);

//Pseq([1,2],2).asList
(
var a;
a = Pseries(start: 0, step: 1, length: inf).asStream;
a.nextN(10);
)

(
var a, b;
a = Pslide([1, 2, 3, 4, 5], inf, 3, 1, 0);
x = a.asStream;
13.do({ x.next.postln; });
)

/////
(
var a;
a = Pslide([1, 7], 3, 1, 2, 0).asStream;
a.nextN(10);
)
/////

(
var a, x;
a = Pfunc({ exprand(0.1, 2.0) + #[1, 2, 3, 6].choose }, { \reset.postln });
x = a.asStream;
x.nextN(20).postln;
x.reset;
)

//// YES!!!! This is it!!!! Pstutter!!!!!
(
// Now we want one thing that squares, the other that halves...
// got it!!!
var a, b;
a = Pstutter(Pgeom(1.0, 2, 4), Pgeom(1.0, 0.5, 4));
x = a.asStream;
15.do({ x.next.postln; });
)


///
(
var a, b, c;
b = Pseq([1], 4);
a = Pseries(start: 1, step: 1, length: 4);
a = Pgeom(1.0, 2, 4);
c = (b/a).asStream;
c.nextN(4);
)
Pseries(1,1,4)

////
(
var a;
a = Pgeom(1.0, 0.5, 4);
a.asStream.nextN(4);
)

////
(
var a;
a = Pgeom(1.0, 0.5, 4);
a.asStream.nextN(4);
)
