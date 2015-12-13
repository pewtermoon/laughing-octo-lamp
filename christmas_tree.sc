//Write sounds to file
(
var g, o;
g = [
    [0.1, [\s_new, \NRTsine, 1000, 0, 0, \freq, 440]],
    [0.2, [\s_new, \NRTsine, 1001, 0, 0, \freq, 660]],
    [0.3, [\s_new, \NRTsine, 1002, 0, 0, \freq, 220]],
    [1, [\c_set, 0, 0]]
    ];
o = ServerOptions.new.numOutputBusChannels = 1; // mono output
Score.recordNRT(g, "help-oscFile.osc", "helpNRT.wav", options: o); // synthesize
)

//File.getcwd
// Use the path of whatever file is currently executing:
//Note: can variables only be defined locally??
//var samplePath;
//samplePath = thisProcess.nowExecutingPath.dirname;
//samplePath.postln;
