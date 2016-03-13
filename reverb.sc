//Reverb examples
s.boot;

a = {SinOsc.ar(440)*0.1}.play
a.run(false)


{LPF.ar(WhiteNoise.ar(0.1000),)}.scope


{LPF.ar(WhiteNoise.ar(0.1),Line.kr(10000,1000,0.5))}.scope

{Resonz.ar(LFNoise0.ar(1000),Line.kr(10000,1000,5),0.1)}.scope

{SinOsc.ar(Line.kr(2000,200,1),0,Line.kr(1,0,1))}.scope

{SinOsc.ar([Line.kr(440,550,10),Line.kr(400,555,10)])}.scope

{Pan2.ar(WhiteNoise.ar(0.1), MouseX.kr(-1,1))}.scope

(1..1.2)

{Mix(SinOsc.ar(500*[0.5,1,1.19,1.56,2,2.51,2.66,3.01,4.1],0,Line.kr(0,0.1,5)))}.scope

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



s.boot;

// FreeVerb2 - demo synthdef
(
SynthDef(\FreeVerb2x2, {|outbus, mix = 0.25, room = 0.15, damp = 0.5, amp = 1.0|
    var signal;

    signal = In.ar(outbus, 2);

    ReplaceOut.ar(outbus,
        FreeVerb2.ar( // FreeVerb2 - true stereo UGen
            signal[0], // Left channel
            signal[1], // Right Channel
            mix, room, damp, amp)); // same params as FreeVerb 1 chn version

}).add;
)

// 2ch source
(
a = SynthDef(\src2x2, {
    Out.ar(0,
        Decay.ar(Impulse.ar(1), 0.25, LFCub.ar(1200, 0, 0.1)) ! 2 +
        Pan2.ar(
            Decay.ar(Impulse.ar(1, pi), 0.1, WhiteNoise.ar(0.1)),
            LFNoise1.kr(0.5).range(-1, 1)
        )
    )
}).play
)

//////
// FreeVerb - 1x1 ugen
(
z = SynthDef(\src, {|mix = 0.25, room = 0.15, damp = 0.5|
    Out.ar(0,
        FreeVerb.ar(
            Decay.ar(Impulse.ar(1), 0.25, LFCub.ar(1200, 0, 0.1)), // mono src
            mix, // mix 0-1
            room, // room 0-1
            damp // damp 0-1 duh
        ) ! 2 // fan out...
    );
}).play
)
z.set(\room, 1)

////
x = Pxrand([1,5/4,3/2,15/8, \rest],32)*440;
p = Pbind(
\instrument, \NRTsine,	\freq,
	Pxrand([x,x*5/4,x*3/2,x*15/8],inf),
	\dur, 0.5,);

/////
(
SynthDef(\sin1, {|freq = 440, amp = 0.5|
	var sig;
	var env;
	sig = SinOsc.ar(freq, 0, amp);
	sig = FreeVerb.ar(sig);
	env = Line.ar(1, 1, 0.2, doneAction: 2);
	sig = sig * env;
	Out.ar(0, sig)
}).add;
)

//speed = 0.2;
(
p = Pbind(\instrument, \sin1,
	\freq, Pseq([440,\rest,220,\rest], 10),
	\dur, Pseq([1,2,1,2]*1,10))


)

play{f=FreeVerb;x=Decay2.ar(Impulse.ar([3,4]),1/99,1/9,LFCub.ar({rrand(40,400)}!4,0,0.1)).sum;5.do{x=f.ar(x,LFTri.kr(1/16,0,1/4,0.3))};x!2}

play{FreeVerb.ar(SinOsc.ar())!2}



(
SynthDef(\smooth, { |freq = 440, sustain = 0.1, amp = 0.5,
	mix = 0, room = 0, damp = 0|
    var sig;
	var env;
    sig = SinOsc.ar(freq, 0, amp);
	env = EnvGen.kr(Env.linen(0.05, sustain, 0.1), doneAction: 2);
	sig = sig * env;
	sig = FreeVerb.ar(sig, mix, room, damp);
    Out.ar(0, sig)
}).add;

(
p = Pbind(
        // the name of the SynthDef to use for each note
    \instrument, \sin1,
    \dur, Pseq([0.2, \rest, 0.2])
).play;
)



