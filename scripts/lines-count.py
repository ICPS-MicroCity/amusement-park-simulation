import pandas as pd
import sys
import glob
import numpy as np


def extract_dataframe(file_name):
    return pd.read_csv(file_name, skiprows=8, header=None, sep="\s+", engine="python")


def extract(df, f):
    return [f(df.loc[i][1:]) for i in range(df.shape[0])]


def main(path):
    file_names = glob.glob(path + "*.csv")
    dfs = [extract_dataframe(file_name) for file_name in file_names]
    results = [extract(df, np.mean) for df in dfs]
    [print(file_names[i], ":", len(results[i])) for i in range(len(file_names))]


if __name__ == "__main__":
    main(sys.argv[1])
