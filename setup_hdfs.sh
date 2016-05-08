sudo apt-get remove openssh-client openssh-server
sudo apt-get install openssh-client openssh-server

ssh -keygen -t dsa -P '' -f ~/.ssh/id_dsa
cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
chmod 0600 ~/.ssh/authorized_keys
