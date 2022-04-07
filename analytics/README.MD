To use the Jupyter Notebook you just need a python server like anaconda and install the jupyter library using pip. 
A simple set of instructions are located here: https://jupyter.org/install

NOTE: Before the Colorado Regional we found the following [Google Sheet on Chief Delphi] (https://www.chiefdelphi.com/t/2022-google-sheets-opr-calculator/404410/2) that we discovered had many of the capabilities and more that we were wanting to pull from the Blue Alliance API during an event.  

With core OPR stats available through the Google Sheet above we were able to focus on other types of data that we could collect in the Robot Scouter app through different templates and collate the data via the exported JSON files in a master phone that had the app, all the templates and all the teams that were shared acrossed all scouts.  With WiFi at the venue for devices without cell service and Android phones that had cell service we were able to sync across data across all the devices the scouts were using.  The basic process and data flow was:

### Thursday, Practice Day:  
1. Used a Pit Strategy template to gather basic capabilities that robots had either by looking them over in the Pits or watching them demonstrate the capability in the practice matches.
2. In the evening when we had a laptop through the master phone we selected all the teams and exported all the data which included an Excel and JSON file.  These JSON files were transferred to the laptop via a direct USB cable connection.  Once on the laptop the PitStrategy Juypter Notebook was used to parse the JSON files and collate together all the Pit Strategies for all the teams that we scouted.
3. Once the Friday's Match schedule was released, the Jupyter Notebook was used to generate a collection of the Pit Strategies for all the teams associated with our matches. [Team Strategies by Match in a separate sheet](https://user-images.githubusercontent.com/5160817/162119953-7045edf8-3b16-4644-8557-9f280bcf9421.png) in an Excel workbook. This workbook with all its sheets was then saved as a PDF and shared to the Drive Team via Slack.
### Friday, Qualification Matches:
1. Scouts used a Match Metrics template to gather key data per team that was not available via the Blue Alliance.  For the 2022 RAPID REACT this was the Cargo data.  What individual teams were able to do with the cargo in terms of shooting.  The Hang and Taxi data was available via the Blue Alliance.  So our Match Metrics template focused on the shooting attempts and percent made within a match for the day.
### Friday evening, Pick List Discussions
1. Similar to Practice Day data the JSON files were exported from the master phone's app, uploaded to a laptop where a Jupyter Notebook was used to parse and collate the data. [Metrics available](https://user-images.githubusercontent.com/5160817/162120524-5d156250-5d08-402e-aa67-3d705d8c892c.png) such as average # of shots and percentage made.
2. Used the Google Sheet's OPR stats sheet to assess 1st Pick options for offensive bots during Alliance Selection.
3. Used the Jupyter Notebook that pulled all the OPR type stats, especially DPRs, to assess 2nd Picks for defensive bots during Alliance Selection.
### Saturday Morning, Remaining Qualification Matches
1. Scouts focused on using the Match Metrics template, but narrowed down to the list of teams that we were interested in for Alliance Selections (up to 10 for offense/1st picks and up to 10 for defense/2nd picks).
2. Was able to provide [per match metrics of shooting stats by team for each match](https://user-images.githubusercontent.com/5160817/162120842-dffbcb14-7fa3-41c5-8ab0-e6c60a96bf8d.png) we were in on Saturday.  Would have been nice to be able to merge this into the [Match Lookup sheet](https://user-images.githubusercontent.com/5160817/162120990-60cd99f1-9f23-461a-90fa-ecfb41fb6fdb.png) that was available in the Google Sheet.  
3. A focused sub-team of Head Coach and Team Captain visited pits to talk to the top 3 teams that we were interested in alliancing with and / or teams that weren't performing but we saw potential to see if we could help.
	
In discussion a week later after the Regional, we were thinking that if Scouts had the Averages from Friday's Matches that had been collected and could just note +/- to these averages and other exceptions like the team had a big fall or they losing more than they were before, THEN that would be more beneficial than just providing actual data.  Especially since the turnaournd of the last qualification matches to Alliance Selection is literally 15 minutes.



