//Reverb examples
s.boot;

(
{
z = Mix.arFill(10, { Resonz.ar(Dust.ar(0.2, 50), Rand(200, 3200), 0.003) });
}.play
)

//{Resonz.ar(Dust.ar(10, 50), Rand(200, 3200),0.003)}.play;
//{Dust.ar(0.2, 50)}.play;
{Resonz.ar(Dust.ar(1, 50), 440, 0.02)}.play;

{Mix.arFill(10, { Resonz.ar(Dust.ar(0.2, 50), Rand(200, 3200), 0.003) })}.play

(
SynthDef("filteredDust", {
	Out.ar(
		2,
		Mix.arFill(10, { Resonz.ar(Dust.ar(0.2, 50), Rand(200, 3200), 0.003) })
	)
}).play;
)

////////////////////////////////////////////////////////////////
b = Buffer.read(s, "sounds/a11wlk01-44_1.aiff");
// Hear it raw:
b.play

//Dude, can just use the free reverbs that come with SC
//i.e. FreeVerb, FreeVerb2 or GVerb (!)


s.quit; //Stops the server

//Some pointless change for testing.