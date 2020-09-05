import matplotlib.pyplot as plt
import argparse
import numpy as np

parser = argparse.ArgumentParser()
parser.add_argument('--file',type=str)
parser.add_argument('--x_name',type=str,default='x')
parser.add_argument('--y_name',type=str,default='y')
args = parser.parse_args()

data = np.loadtxt(args.file)

y = data[:,0]
# y = np.log10(y)
x = data[:,1]
plt.plot(x,y)
plt.xlabel(args.x_name)
plt.ylabel(args.y_name)
plt.show()