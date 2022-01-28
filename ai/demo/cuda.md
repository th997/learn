
# cuda install

## driver install
sudo echo "blacklist nouveau" >> /etc/modprobe.d/blacklist.conf

sudo update-initramfs -u

sudo apt-get install build-essential

sudo apt-get purge nvidia*

sudo reboot now 

https://www.nvidia.com/Download/index.aspx?lang=en-us

nvidia-smi

## cuda 
https://developer.nvidia.com/cuda-downloads

wget https://developer.download.nvidia.com/compute/cuda/11.4.2/local_installers/cuda_11.4.2_470.57.02_linux.run

sudo sh cuda_11.4.2_470.57.02_linux.run

export PATH=/usr/local/cuda/bin:$PATH

nvcc -V

## cudnn
conda install cudnn cudatoolkit numba
