import pandas as pd
import sys
import glob
import numpy as np


def extract_dataframe(file_name):
    return pd.read_csv(file_name, skiprows=8, header=None, sep="\s+", engine="python")


def extract(df, f, size):
    return [f(df.loc[i][1:]) for i in range(size)]


def main(path, data_type, strategy):
    file_names = glob.glob(path + data_type + "*\"" + strategy + "\"*.csv")
    dfs = [extract_dataframe(file_name) for file_name in file_names]
    ops = [np.mean, np.sum, np.var]
    results = [[extract(df, op, 22) for df in dfs] for op in ops]
    print("Means:", np.mean(results[0], axis=0))
    print("Variances:", np.mean(results[2], axis=0))
    # print("Sums:", np.mean(results[1], axis=0))


if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], sys.argv[3])
