#!/usr/bin/env python3
import subprocess
import getpass

def get(command):
    return subprocess.check_output(["/bin/bash", "-c", command]).decode("utf-8")


# calculate screen resolution
res_output = get("xrandr").split() 
idf = res_output.index("current")
res = (int(res_output[idf+1]), int(res_output[idf+3].replace(",", "")))
# creating window list on current viewport / id's / application names
w_data = [l.split()[0:7] for l in get("wmctrl -lpG").splitlines()]
windows = [get("ps -u "+getpass.getuser()+" | grep "+w[2]).split()[-1]
           for w in w_data if 0 < int(w[3]) < res[0] and 0 < int(w[4]) < res[1]]
t=""
for p in windows:
 t = t + "\""+p+"\","
t = t[:-1]
print t

# preparing zenity optionlist
#print windows
